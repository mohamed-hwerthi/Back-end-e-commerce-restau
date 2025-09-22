package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.exception.DuplicateMenuItemException;
import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.model.dto.DiscountType;
import com.foodsquad.FoodSquad.model.dto.MenuItemFilterByCategoryAndQueryRequestDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
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

import java.math.BigDecimal;
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

    private final ProductMapper productMapper;

    private final TaxService taxService;

    private final MenuItemDiscountPriceCalculator menuItemDiscountPriceCalculator;

    private final MediaService mediaService;

    private final LocaleContext localeContext;


    public MenuItemServiceImp(MenuItemRepository menuItemRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, ModelMapper modelMapper, TaxRepository taxRepository, @Lazy MenuItemPromotionSharedService menuItemPromotionSharedService, ProductMapper productMapper, TaxService taxService, MenuItemDiscountPriceCalculator menuItemDiscountPriceCalculator, MediaService mediaService, LocaleContext localeContext) {

        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.taxRepository = taxRepository;
        this.menuItemPromotionSharedService = menuItemPromotionSharedService;
        this.productMapper = productMapper;
        this.taxService = taxService;
        this.menuItemDiscountPriceCalculator = menuItemDiscountPriceCalculator;
        this.mediaService = mediaService;
        this.localeContext = localeContext;
    }


    public ProductDTO createMenuItem(ProductDTO productDTO) {

        logger.debug("Creating menu item: {}", productDTO);
        if (StringUtils.hasText(productDTO.getBarCode()) && isMenuItemExistByBarCode(productDTO.getBarCode())) {
            throw new DuplicateMenuItemException("Menu item with bar code " + productDTO.getBarCode() + " already exists");
        }
        Product product = productMapper.toEntity(productDTO);
        if (productDTO.getTax() != null) {
            Tax tax = this.taxService.createTax(productDTO);
            product.setTax(tax);
            BigDecimal price = product.getPrice();
            BigDecimal taxRate = BigDecimal.valueOf((productDTO.getTax().getRate()) / 100);
            BigDecimal priceWithTax = price.multiply(BigDecimal.ONE.add(taxRate));
            product.setPrice(priceWithTax);
        }
        Product savedMenuItem = menuItemRepository.save(product);
        return productMapper.toDto(savedMenuItem);
    }


    private boolean isMenuItemExistByBarCode(String barCode) {

        return menuItemRepository.findByBarCode(barCode).isPresent();
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> searchMenuItemsByQuery(
            MenuItemFilterByCategoryAndQueryRequestDTO filterRequest,
            Pageable pageable
    ) {
        String query = filterRequest.getQuery();
        UUID[] categoryIds = filterRequest.getCategoriesIds() != null
                ? filterRequest.getCategoriesIds().toArray(new UUID[0])
                : null;
        Boolean inStock = filterRequest.getInStock() != null ? filterRequest.getInStock() : null;

        Page<Product> menuItemPage = menuItemRepository.searchByQueryAndFilters(
                query,
                categoryIds,
                inStock,
                localeContext.getLocale(),
                pageable
        );

        List<ProductDTO> menuItemDTOs = menuItemPage.getContent().stream()
                .map(productMapper::toDto)
                .toList();

        return new PaginatedResponseDTO<>(menuItemDTOs, menuItemPage.getTotalElements());
    }


    @Override
    public PaginatedResponseDTO<ProductDTO> searchMenuItemsByQuery(String query, Pageable pageable) {
        logger.debug("Searching menu items by query: {}", query);

  /*
  todo   : we have to change it to find all until  we Fix all Trasnaltiosn
   */
        Page<Product> menuItemPage = menuItemRepository.findAll(pageable);
        List<Product> products = menuItemPage.getContent();
        List<ProductDTO> menuItemDTOs = products.stream()
                .map(product -> {
                    ProductDTO dto = productMapper.toDto(product);
                    return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, dto);
                })
                .toList();
        return new PaginatedResponseDTO<>(menuItemDTOs, menuItemPage.getTotalElements());


    }

    public ProductDTO getMenuItemById(UUID id) {

        logger.debug("Getting menu item by ID: {}", id);

        Product product = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
        Integer salesCount = orderRepository.sumQuantityByMenuItemId(product.getId());
        if (salesCount == null) {
            salesCount = 0;
        }
        long reviewCount = reviewRepository.countByProductId(product.getId());
        Double averageRating = reviewRepository.findAverageRatingByProductId(product.getId());
        if (averageRating == null) {
            averageRating = 0.0;
        }
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        ProductDTO productDTO = productMapper.toMenuItemDtoWithMoreInformation(product, salesCount, reviewCount, averageRating);

        //  return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, productDTO);
        return productDTO;

    }

    public PaginatedResponseDTO<ProductDTO> getAllMenuItems(
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
        Page<Product> menuItemPage;
        if (categoryId != null) {
            menuItemPage = menuItemRepository.findByCategoryId(categoryId, pageable);
        } else {
            menuItemPage = menuItemRepository.findAll(pageable);
        }
//        List<ProductDTO> products = menuItemPage.stream()
//                .map(product -> {
//                    Integer salesCount = orderRepository.sumQuantityByMenuItemId(product.getId());
//                    if (salesCount == null) {
//                        salesCount = 0;
//                    }
//
//                    long reviewCount = reviewRepository.countByMenuItemId(product.getId());
//                    Double averageRating = reviewRepository.findAverageRatingByMenuItemId(product.getId());
//                    if (averageRating == null) {
//                        averageRating = 0.0;
//                    }
//                    averageRating = Math.round(averageRating * 10.0) / 10.0;
//                    ProductDTO productDTO = productMapper.toMenuItemDtoWithMoreInformation(
//                            product, salesCount, reviewCount, averageRating
//                    );
//
//                    return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, productDTO);
//                })
//                .collect(Collectors.toList());

//        if (priceSortDirection != null && !priceSortDirection.isEmpty()) {
//            products.sort((a, b) -> {
//                if (priceSortDirection.equalsIgnoreCase("asc")) {
//                    return Double.compare(a.getPrice(), b.getPrice());
//                } else {
//                    return Double.compare(b.getPrice(), a.getPrice());
//                }
//            });
//        } else if ("salesCount".equalsIgnoreCase(sortBy)) {
//            products.sort((item1, item2) -> desc
//                    ? item2.getSalesCount().compareTo(item1.getSalesCount())
//                    : item1.getSalesCount().compareTo(item2.getSalesCount()));
//        }

        return new PaginatedResponseDTO<>(menuItemPage.getContent().stream().map(productMapper::toDto).toList(), menuItemPage.getTotalElements());
    }

    @Transactional
    public ResponseEntity<ProductDTO> updateMenuItem(UUID id, ProductDTO productDTO) {

        logger.debug("Updating menu item with ID: {}", id);
        Product existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));

        if (productDTO.getTax() != null) {
            Tax existingTax = existingMenuItem.getTax();
            BigDecimal newPrice = productDTO.getPrice();
            double newTaxRate = productDTO.getTax().getRate();
            double existingTaxRate = (existingTax != null) ? existingTax.getRate() : 0;
            BigDecimal existingPriceTTC = existingMenuItem.getPrice();
            Tax tax = existingTax != null ? existingTax : new Tax();
            tax.setRate(newTaxRate);
            tax.setName(productDTO.getTax().getName());
            tax = taxRepository.save(tax);
            existingMenuItem.setTax(tax);
        }

        productMapper.updateMenuItemFromDto(productDTO, existingMenuItem);
        Product savedMenuItem = menuItemRepository.save(existingMenuItem);
        ProductDTO responseDTO = modelMapper.map(savedMenuItem, ProductDTO.class);

        return ResponseEntity.ok(responseDTO);
    }


    @Override
    public ProductDTO decrementMenuItemQuantity(UUID menuItemId, int quantity) {

        Product product = menuItemRepository.findById(menuItemId).orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + menuItemId));
        if (product.getQuantity() < quantity) {
            throw new EntityNotFoundException("Product quantity is not enough");
        }
        product.setQuantity(product.getQuantity() - quantity);
        return productMapper.toDto(menuItemRepository.save(product));

    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteMenuItem(UUID id) {

        Product product = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
        orderRepository.removeMenuItemReferences(product.getId());

        menuItemRepository.delete(product);
        return ResponseEntity.ok(Map.of("message", "Menu Item successfully deleted"));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteMenuItemsByIds(List<UUID> ids) {

        List<Product> products = menuItemRepository.findAllById(ids);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("No MenuItems found for the given IDs");
        }
        products.forEach(product -> {
            orderRepository.removeMenuItemReferences(product.getId());
            menuItemRepository.delete(product);
        });
        return ResponseEntity.ok(Map.of("message", "Menu Items successfully deleted"));
    }


    public ResponseEntity<List<ProductDTO>> getMenuItemsByIds(List<UUID> ids) {

        logger.debug("Getting menu items by IDs: {}", ids);

        List<Product> products = menuItemRepository.findAllById(ids);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("No MenuItems found for the given IDs");
        }
        List<ProductDTO> menuItemDTOs = products.stream()
                .map(product -> {
                    Integer salesCount = orderRepository.sumQuantityByMenuItemId(product.getId());
                    if (salesCount == null) {
                        salesCount = 0;
                    }
                    long reviewCount = reviewRepository.countByProductId(product.getId());
                    Double averageRating = reviewRepository.findAverageRatingByProductId(product.getId());
                    if (averageRating == null) {
                        averageRating = 0.0;
                    }
                    averageRating = Math.round(averageRating * 10.0) / 10.0;


                    ProductDTO productDTO = productMapper.toMenuItemDtoWithMoreInformation(product, salesCount, reviewCount, averageRating);
                    return verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, productDTO);
                })
                .toList();
        return ResponseEntity.ok(menuItemDTOs);
    }

    @Override
    public ProductDTO findByBarCode(String barCode) {

        return menuItemRepository.findByBarCode(barCode)
                .map(productMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for barCode"));
    }

    private double roundToScale(double value, int scale) {

        if (scale < 0) throw new IllegalArgumentException("Scale must be a positive integer");

        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }

    @Override
    public List<ProductDTO> saveMenuItems(List<Product> products) {

        return menuItemRepository.saveAll(products).stream().map(productMapper::toDto).toList();
    }

    @Override
    public ProductDTO save(Product product) {

        return productMapper.toDto(menuItemRepository.save(product));
    }

    @Override
    public Product findMenuItemById(UUID id) {

        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
    }

    @Override
    public BigDecimal findMenuItemDiscountedPrice(UUID menuItemId) {

        Product product = menuItemRepository.findById(menuItemId).orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + menuItemId));
        return menuItemDiscountPriceCalculator.calculateDiscountedPrice(product);
    }

    @Override
    public List<Product> findByPromotion(Promotion promotion) {

        return menuItemRepository.findAllByPromotionsContaining(promotion);
    }

    private ProductDTO verifyMenuItemIsPromotedForCurrentDayAndCalculateDiscountedPrice(Product product, ProductDTO productDTO) {

        boolean hasActivePromotion = menuItemPromotionSharedService.isMenuItemHasActivePromotionInCurrentDay(product.getId());
        productDTO.setPromoted(hasActivePromotion);

        BigDecimal discountedPrice = product.getPrice();

        if (hasActivePromotion) {
            discountedPrice = menuItemDiscountPriceCalculator.calculateDiscountedPrice(product);
        }
        productDTO.setDiscountedPrice(discountedPrice);
        return productDTO;
    }


    @Override
    public List<Product> findByCategory(Category category) {

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

        Product product = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + menuItemId + " not found"));

        product.getMedias().removeIf(media -> media.getId().equals(mediaId));

        menuItemRepository.save(product);

        mediaService.deleteMedia(mediaId);

        logger.info("Successfully deleted media {} for menu item {}", mediaId, menuItemId);
    }

}
