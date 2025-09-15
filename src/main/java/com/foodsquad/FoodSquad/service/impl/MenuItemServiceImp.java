package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.exception.DuplicateMenuItemException;
import com.foodsquad.FoodSquad.mapper.MenuItemMapper;
import com.foodsquad.FoodSquad.model.dto.DiscountType;
import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.MenuItemFilterByCategoryAndQueryRequestDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.MenuItemRepository;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.repository.TaxRepository;
import com.foodsquad.FoodSquad.service.declaration.MediaService;
import com.foodsquad.FoodSquad.service.declaration.MenuItemPromotionSharedService;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import com.foodsquad.FoodSquad.service.declaration.TaxService;
import com.foodsquad.FoodSquad.service.helpers.MenuItemDiscountPriceCalculator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MenuItemServiceImp implements MenuItemService {

    private final Logger logger = LoggerFactory.getLogger(MenuItemServiceImp.class);

    private final MenuItemRepository menuItemRepository;

    private final OrderRepository orderRepository;

    private final ReviewRepository reviewRepository;


    private final ModelMapper modelMapper;

    private final TaxRepository taxRepository;


    private final MenuItemPromotionSharedService menuItemPromotionSharedService;

    private final MenuItemMapper menuItemMapper;

    private final TaxService taxService;

    private final MenuItemDiscountPriceCalculator menuItemDiscountPriceCalculator;

    private final MediaService mediaService;


    public MenuItemServiceImp(MenuItemRepository menuItemRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, ModelMapper modelMapper, TaxRepository taxRepository, @Lazy MenuItemPromotionSharedService menuItemPromotionSharedService, MenuItemMapper menuItemMapper, TaxService taxService, MenuItemDiscountPriceCalculator menuItemDiscountPriceCalculator, MediaService mediaService) {

        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.taxRepository = taxRepository;
        this.menuItemPromotionSharedService = menuItemPromotionSharedService;
        this.menuItemMapper = menuItemMapper;
        this.taxService = taxService;
        this.menuItemDiscountPriceCalculator = menuItemDiscountPriceCalculator;
        this.mediaService = mediaService;
    }


    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {

        logger.debug("Creating menu item: {}", menuItemDTO);
        if (StringUtils.hasText(menuItemDTO.getBarCode()) && isMenuItemExistByBarCode(menuItemDTO.getBarCode())) {
            throw new DuplicateMenuItemException("Menu item with bar code " + menuItemDTO.getBarCode() + " already exists");
        }
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        if (menuItemDTO.getTax() != null) {
            Tax tax = this.taxService.createTax(menuItemDTO);
            menuItem.setTax(tax);
            double price = menuItem.getPrice();
            double taxRate = (menuItemDTO.getTax().getRate()) / 100;
            double priceWithTax = price * (1 + taxRate);
            menuItem.setPrice(priceWithTax);
        }
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(savedMenuItem);
    }


    private boolean isMenuItemExistByBarCode(String barCode) {

        return menuItemRepository.findByBarCode(barCode).isPresent();
    }

    @Override
    public PaginatedResponseDTO<MenuItemDTO> searchMenuItemsByQuery(MenuItemFilterByCategoryAndQueryRequestDTO menuItemFilterByCategoryAndQueryRequestDTO, Pageable pageable) {

        logger.debug("Searching menu items by query and categoriesIds ");
        Page<MenuItem> menuItemPage;
        if (ObjectUtils.isEmpty(menuItemFilterByCategoryAndQueryRequestDTO.getCategoriesIds())) {
            menuItemPage = menuItemRepository.findByQuery(menuItemFilterByCategoryAndQueryRequestDTO.getQuery(), pageable);

        } else {
            menuItemPage = menuItemRepository.filterByQueryAndCategories(menuItemFilterByCategoryAndQueryRequestDTO.getQuery(), menuItemFilterByCategoryAndQueryRequestDTO.getCategoriesIds(), pageable);

        }

        List<MenuItem> menuItems = menuItemPage.getContent();
        List<MenuItemDTO> menuItemDTOs = menuItems.stream()
                .map(menuItemMapper::toDto)
                .toList();
        return new PaginatedResponseDTO<>(menuItemDTOs, menuItemPage.getTotalElements());


    }

    @Override
    public PaginatedResponseDTO<MenuItemDTO> searchMenuItemsByQuery(String query, Pageable pageable) {

        logger.debug("Searching menu items by query: {}", query);


        Page<MenuItem> menuItemPage = menuItemRepository.findByQuery(query, pageable);
        List<MenuItem> menuItems = menuItemPage.getContent();
        List<MenuItemDTO> menuItemDTOs = menuItems.stream()
                .map(menuItem -> {
                    MenuItemDTO dto = menuItemMapper.toDto(menuItem);
                    return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(menuItem, dto);
                })
                .toList();
        return new PaginatedResponseDTO<>(menuItemDTOs, menuItemPage.getTotalElements());


    }

    public MenuItemDTO getMenuItemById(UUID id) {

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

        MenuItemDTO menuItemDTO = menuItemMapper.toMenuItemDtoWithMoreInformation(menuItem, salesCount, reviewCount, averageRating);

        //  return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(menuItem, menuItemDTO);
        return menuItemDTO;

    }

    public PaginatedResponseDTO<MenuItemDTO> getAllMenuItems(
            int page,
            int limit,
            String sortBy,
            boolean desc,
            UUID categoryId,
            String isDefault,
            String priceSortDirection
    ) {
        logger.debug("Getting all menu items with some filters");

        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";

        Sort.Direction direction = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction, sortField));
        Page<MenuItem> menuItemPage;
        if (categoryId != null) {
            menuItemPage = menuItemRepository.findByCategoryId(categoryId, pageable);
        } else {
            menuItemPage = menuItemRepository.findAll(pageable);
        }
//        List<MenuItemDTO> menuItems = menuItemPage.stream()
//                .map(menuItem -> {
//                    Integer salesCount = orderRepository.sumQuantityByMenuItemId(menuItem.getId());
//                    if (salesCount == null) {
//                        salesCount = 0;
//                    }
//
//                    long reviewCount = reviewRepository.countByMenuItemId(menuItem.getId());
//                    Double averageRating = reviewRepository.findAverageRatingByMenuItemId(menuItem.getId());
//                    if (averageRating == null) {
//                        averageRating = 0.0;
//                    }
//                    averageRating = Math.round(averageRating * 10.0) / 10.0;
//                    MenuItemDTO menuItemDTO = menuItemMapper.toMenuItemDtoWithMoreInformation(
//                            menuItem, salesCount, reviewCount, averageRating
//                    );
//
//                    return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(menuItem, menuItemDTO);
//                })
//                .collect(Collectors.toList());

//        if (priceSortDirection != null && !priceSortDirection.isEmpty()) {
//            menuItems.sort((a, b) -> {
//                if (priceSortDirection.equalsIgnoreCase("asc")) {
//                    return Double.compare(a.getPrice(), b.getPrice());
//                } else {
//                    return Double.compare(b.getPrice(), a.getPrice());
//                }
//            });
//        } else if ("salesCount".equalsIgnoreCase(sortBy)) {
//            menuItems.sort((item1, item2) -> desc
//                    ? item2.getSalesCount().compareTo(item1.getSalesCount())
//                    : item1.getSalesCount().compareTo(item2.getSalesCount()));
//        }

        return new PaginatedResponseDTO<>(menuItemPage.getContent().stream().map(menuItemMapper::toDto).toList(), menuItemPage.getTotalElements());
    }

    @Transactional
    public ResponseEntity<MenuItemDTO> updateMenuItem(UUID id, MenuItemDTO menuItemDTO) {

        logger.debug("Updating menu item with ID: {}", id);
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + id));

        if (menuItemDTO.getTax() != null) {
            Tax existingTax = existingMenuItem.getTax();
            double newPrice = menuItemDTO.getPrice();
            double newTaxRate = menuItemDTO.getTax().getRate();
            double existingTaxRate = (existingTax != null) ? existingTax.getRate() : 0;
            double existingPriceTTC = existingMenuItem.getPrice();
            Tax tax = existingTax != null ? existingTax : new Tax();
            tax.setRate(newTaxRate);
            tax.setName(menuItemDTO.getTax().getName());
            tax = taxRepository.save(tax);
            existingMenuItem.setTax(tax);
        }

        menuItemMapper.updateMenuItemFromDto(menuItemDTO, existingMenuItem);
        MenuItem savedMenuItem = menuItemRepository.save(existingMenuItem);
        MenuItemDTO responseDTO = modelMapper.map(savedMenuItem, MenuItemDTO.class);

        return ResponseEntity.ok(responseDTO);
    }


    @Override
    public MenuItemDTO decrementMenuItemQuantity(UUID menuItemId, int quantity) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + menuItemId));
        if (menuItem.getQuantity() < quantity) {
            throw new EntityNotFoundException("MenuItem quantity is not enough");
        }
        menuItem.setQuantity(menuItem.getQuantity() - quantity);
        return menuItemMapper.toDto(menuItemRepository.save(menuItem));

    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteMenuItem(UUID id) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + id));
        orderRepository.removeMenuItemReferences(menuItem.getId());

        menuItemRepository.delete(menuItem);
        return ResponseEntity.ok(Map.of("message", "Menu Item successfully deleted"));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteMenuItemsByIds(List<UUID> ids) {

        List<MenuItem> menuItems = menuItemRepository.findAllById(ids);
        if (menuItems.isEmpty()) {
            throw new EntityNotFoundException("No MenuItems found for the given IDs");
        }
        menuItems.forEach(menuItem -> {
            orderRepository.removeMenuItemReferences(menuItem.getId());
            menuItemRepository.delete(menuItem);
        });
        return ResponseEntity.ok(Map.of("message", "Menu Items successfully deleted"));
    }


    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByIds(List<UUID> ids) {

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
                        averageRating = 0.0;
                    }
                    averageRating = Math.round(averageRating * 10.0) / 10.0;


                    MenuItemDTO menuItemDTO = menuItemMapper.toMenuItemDtoWithMoreInformation(menuItem, salesCount, reviewCount, averageRating);
                    return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(menuItem, menuItemDTO);
                })
                .toList();
        return ResponseEntity.ok(menuItemDTOs);
    }

    @Override
    public MenuItemDTO findByBarCode(String barCode) {

        return menuItemRepository.findByBarCode(barCode)
                .map(menuItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for barCode"));
    }

    private double roundToScale(double value, int scale) {

        if (scale < 0) throw new IllegalArgumentException("Scale must be a positive integer");

        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }

    @Override
    public List<MenuItemDTO> saveMenuItems(List<MenuItem> menuItems) {

        return menuItemRepository.saveAll(menuItems).stream().map(menuItemMapper::toDto).toList();
    }

    @Override
    public MenuItemDTO save(MenuItem menuItem) {

        return menuItemMapper.toDto(menuItemRepository.save(menuItem));
    }

    @Override
    public MenuItem findMenuItemById(UUID id) {

        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + id));
    }

    @Override
    public Double findMenuItemDiscountedPrice(UUID menuItemId) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new EntityNotFoundException("MenuItem not found for ID: " + menuItemId));
        return menuItemDiscountPriceCalculator.calculateDiscountedPrice(menuItem);
    }

    @Override
    public List<MenuItem> findByPromotion(Promotion promotion) {

        return menuItemRepository.findAllByPromotionsContaining(promotion);
    }

    private MenuItemDTO verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(MenuItem menuItem, MenuItemDTO menuItemDTO) {

        boolean hasActivePromotion = menuItemPromotionSharedService.isMenuItemHasActivePromotionInCurrentDay(menuItem.getId());
        menuItemDTO.setPromoted(hasActivePromotion);

        double discountedPrice = menuItem.getPrice();

        if (hasActivePromotion) {
            discountedPrice = menuItemDiscountPriceCalculator.calculateDiscountedPrice(menuItem);
        }
        menuItemDTO.setDiscountedPrice(discountedPrice);
        return menuItemDTO;
    }


    @Override
    public List<MenuItem> findByCategory(Category category) {

        return menuItemRepository.findAllByCategoriesContaining(category);
    }


    private boolean isPromotionDiscountTypeByPercentage(PercentageDiscountPromotion percentageDiscountPromotion) {

        return !ObjectUtils.isEmpty(percentageDiscountPromotion) && percentageDiscountPromotion.getDiscountType().equals(DiscountType.BY_PERCENTAGE);
    }

    private boolean isPromotionDiscountTypeByAmount(PercentageDiscountPromotion percentageDiscountPromotion) {

        return !ObjectUtils.isEmpty(percentageDiscountPromotion) && percentageDiscountPromotion.getDiscountType().equals(DiscountType.BY_AMOUNT);
    }

    @Override
    public void deleteMediaForMenuItem(UUID menuItemId, UUID mediaId) {
        logger.debug("Deleting media {} for menu item {}", mediaId, menuItemId);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem with id " + menuItemId + " not found"));

        menuItem.getMedias().removeIf(media -> media.getId().equals(mediaId));

        menuItemRepository.save(menuItem);

        mediaService.deleteMedia(mediaId);

        logger.info("Successfully deleted media {} for menu item {}", mediaId, menuItemId);
    }

}
