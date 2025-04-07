package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CategoryMapper;
import com.foodsquad.FoodSquad.mapper.MediaMapper;
import com.foodsquad.FoodSquad.mapper.MenuItemMapper;
import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.MenuItemCategory;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.MenuItemRepository;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.CategoryService;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImp implements MenuItemService {

    private final Logger logger = LoggerFactory.getLogger(MenuItemService.class);

    private final MenuItemRepository menuItemRepository;

    private final OrderRepository orderRepository;

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    private final MenuItemMapper menuItemMapper;
    private final MediaMapper mediaMapper ;

    public MenuItemServiceImp(MenuItemRepository menuItemRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, UserRepository userRepository, ModelMapper modelMapper, CategoryService categoryService, CategoryMapper categoryMapper, MenuItemMapper menuItemMapper, MediaMapper mediaMapper) {

        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.menuItemMapper = menuItemMapper;
        this.mediaMapper = mediaMapper;
    }

    private User getCurrentUser() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void checkOwnership(MenuItem menuItem) {

        User currentUser = getCurrentUser();
        if (!menuItem.getUser().equals(currentUser) && !currentUser.getRole().equals(UserRole.ADMIN) && !currentUser.getRole().equals(UserRole.MODERATOR)) {
            throw new IllegalArgumentException("Access denied");
        }
    }

    public ResponseEntity<MenuItemDTO> createMenuItem(MenuItemDTO menuItemDTO) {

        logger.debug("Creating menu item: {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        User currentUser = getCurrentUser();
        menuItem.setUser(currentUser);
        if (menuItem.getDefaultItem() == null) {
            menuItem.setDefaultItem(false);
        }
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        MenuItemDTO responseDTO = menuItemMapper.toDto(savedMenuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public PaginatedResponseDTO<MenuItemDTO> searchMenuItemsByQuery(String query, Pageable pageable) {

        logger.debug("Searching menu items by query: {}", query);
        Page<MenuItem> menuItemPage = menuItemRepository.filterByQuery(query, pageable);
        List<MenuItem> menuItems = menuItemPage.getContent();
        List<MenuItemDTO> menuItemDTOs = menuItems.stream()
                .map(menuItemMapper::toDto)
                .toList();
        return new PaginatedResponseDTO<>(menuItemDTOs, menuItemPage.getTotalElements());


    }

    public ResponseEntity<MenuItemDTO> getMenuItemById(Long id) {

        logger.debug("Getting menu item by ID: {}", id);

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + id));
        Integer salesCount = orderRepository.sumQuantityByMenuItemId(menuItem.getId());
        if (salesCount == null) {
            salesCount = 0;
        }
        long reviewCount = reviewRepository.countByMenuItemId(menuItem.getId());
        Double averageRating = reviewRepository.findAverageRatingByMenuItemId(menuItem.getId());
        if (averageRating == null) {
            averageRating = 0.0;
        }
        averageRating = Math.round(averageRating * 10.0) / 10.0;
        List<CategoryDTO>menuItemCategories = menuItem.getCategories().stream().map(categoryMapper::toDto).toList();
            List<MediaDTO>menuItemMedia = menuItem.getMedias().stream().map(mediaMapper::toDto).toList();
        MenuItemDTO menuItemDTO = new MenuItemDTO(menuItem, salesCount, reviewCount, averageRating , menuItemCategories , menuItemMedia);
        return ResponseEntity.ok(menuItemDTO);
    }

    public PaginatedResponseDTO<MenuItemDTO> getAllMenuItems(int page, int limit, String sortBy, boolean desc, Long categoryId, String isDefault, String priceSortDirection) {

        logger.debug("Getting all menu items  with some filters");

        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdOn"));
        Page<MenuItem> menuItemPage ;

        if (categoryId != null ) {
            menuItemPage = menuItemRepository.findByCategoryId(categoryId, pageable);
        } else {
            menuItemPage = menuItemRepository.findAll(pageable);
        }
        List<MenuItemDTO> menuItems = menuItemPage.stream()
                .map(menuItem -> {
                    Integer salesCount = orderRepository.sumQuantityByMenuItemId(menuItem.getId());
                    if (salesCount == null) {
                        salesCount = 0;
                    }
                    long reviewCount = reviewRepository.countByMenuItemId(menuItem.getId());
                    Double averageRating = reviewRepository.findAverageRatingByMenuItemId(menuItem.getId());
                    if (averageRating == null) {
                        averageRating = 0.0; // Default to 0.0 if there are no reviews
                    }
                    averageRating = Math.round(averageRating * 10.0) / 10.0; // Format to 1 decimal place
                    List<CategoryDTO> menuItemCategories = menuItem.getCategories().stream().map(categoryMapper::toDto).toList();
                    List<MediaDTO>menuItemMedia = menuItem.getMedias().stream().map(mediaMapper::toDto).toList();

                    return new MenuItemDTO(menuItem, salesCount, reviewCount, averageRating , menuItemCategories , menuItemMedia);
                })
                .collect(Collectors.toList());

        // Filter by default item
        if (isDefault != null && !isDefault.isEmpty()) {
            boolean defaultFlag = isDefault.equalsIgnoreCase("true");
            menuItems = menuItems.stream().filter(item -> item.getDefaultItem().equals(defaultFlag)).collect(Collectors.toList());
        }

        // Sort by price
        if (priceSortDirection != null && !priceSortDirection.isEmpty()) {
            menuItems.sort((a, b) -> {
                if (priceSortDirection.equals("asc")) {
                    return Double.compare(a.getPrice(), b.getPrice());
                } else {
                    return Double.compare(b.getPrice(), a.getPrice());
                }
            });
        }

        // Sort by salesCount if specified (workaround for now)
        if ("salesCount".equals(sortBy)) {
            menuItems.sort((item1, item2) -> desc
                    ? item2.getSalesCount().compareTo(item1.getSalesCount())
                    : item1.getSalesCount().compareTo(item2.getSalesCount()));
        }

        // PaginatedResponseDTO with the list of items and the total count
        return new PaginatedResponseDTO<>(menuItems, menuItemPage.getTotalElements());
    }

    @Transactional
    public ResponseEntity<MenuItemDTO> updateMenuItem(Long id, MenuItemDTO menuItemDTO) {
        logger.debug("Updating menu item with ID: {}", id);

        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + id));

        checkOwnership(existingMenuItem);

        MenuItem menuItemToUpdate = menuItemMapper.toEntity(menuItemDTO);
        menuItemToUpdate.setId(existingMenuItem.getId());
        menuItemToUpdate.setUser(existingMenuItem.getUser());

        if (menuItemToUpdate.getDefaultItem() == null) {
            menuItemToUpdate.setDefaultItem(false);
        }

        MenuItem savedMenuItem = menuItemRepository.save(menuItemToUpdate);
        MenuItemDTO responseDTO = modelMapper.map(savedMenuItem, MenuItemDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteMenuItem(Long id) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + id));
        checkOwnership(menuItem);

        orderRepository.removeMenuItemReferences(menuItem.getId());

        menuItemRepository.delete(menuItem);
        return ResponseEntity.ok(Map.of("message", "Menu Item successfully deleted"));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteMenuItemsByIds(List<Long> ids) {

        List<MenuItem> menuItems = menuItemRepository.findAllById(ids);
        if (menuItems.isEmpty()) {
            throw new EntityNotFoundException("No MenuItems found for the given IDs");
        }
        menuItems.forEach(menuItem -> {
            checkOwnership(menuItem);
            orderRepository.removeMenuItemReferences(menuItem.getId()); // Remove references in order_menu_item table
            menuItemRepository.delete(menuItem);
        });
        return ResponseEntity.ok(Map.of("message", "Menu Items successfully deleted"));
    }


    //  method to fetch multiple menu items by their IDs
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByIds(List<Long> ids) {

        logger.debug("Getting menu items by IDs: {}", ids);

        List<MenuItem> menuItems = menuItemRepository.findAllById(ids);
        if (menuItems.isEmpty()) {
            throw new EntityNotFoundException("No MenuItems found for the given IDs");
        }
        List<MenuItemDTO> menuItemDTOs = menuItems.stream()
                .map(menuItem -> {
                    Integer salesCount = orderRepository.sumQuantityByMenuItemId(menuItem.getId());
                    if (salesCount == null) {
                        salesCount = 0;
                    }
                    long reviewCount = reviewRepository.countByMenuItemId(menuItem.getId());
                    Double averageRating = reviewRepository.findAverageRatingByMenuItemId(menuItem.getId());
                    if (averageRating == null) {
                        averageRating = 0.0; // Default to 0.0 if there are no reviews
                    }
                    averageRating = Math.round(averageRating * 10.0) / 10.0; // Format to 1 decimal place
                    List<CategoryDTO> menuItemCategories = menuItem.getCategories().stream().map(categoryMapper::toDto).toList();
                    List<MediaDTO>menuItemMedia = menuItem.getMedias().stream().map(mediaMapper::toDto).toList();


                    return new MenuItemDTO(menuItem, salesCount, reviewCount, averageRating , menuItemCategories ,menuItemMedia);
                })
                .toList();
        return ResponseEntity.ok(menuItemDTOs);
    }

    @Override
    public MenuItemDTO findByBarCode(String qrCode) {

        return menuItemRepository.findByBarCode(qrCode)
                .map(menuItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for qrCode"));
    }


}
