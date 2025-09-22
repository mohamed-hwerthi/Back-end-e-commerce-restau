package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.exception.DuplicateProductException;
import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.model.dto.*;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.repository.TaxRepository;
import com.foodsquad.FoodSquad.service.declaration.*;
import com.foodsquad.FoodSquad.service.helpers.ProductDiscountPriceCalculator;
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
public class ProductServiceImp implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final ReviewRepository reviewRepository;


    private final ModelMapper modelMapper;

    private final TaxRepository taxRepository;


    private final ProductPromotionSharedService ProductPromotionSharedService;

    private final ProductMapper productMapper;

    private final TaxService taxService;

    private final ProductDiscountPriceCalculator ProductDiscountPriceCalculator;

    private final MediaService mediaService;

    private final LocaleContext localeContext;

    private final  ProductAttributeService productAttributeService;

    private final  ProductAttributeValueService productAttributeValueService;


    public ProductServiceImp(ProductRepository ProductRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, ModelMapper modelMapper, TaxRepository taxRepository, @Lazy ProductPromotionSharedService ProductPromotionSharedService, ProductMapper productMapper, TaxService taxService, ProductDiscountPriceCalculator ProductDiscountPriceCalculator, MediaService mediaService, LocaleContext localeContext, ProductAttributeService productAttributeService, ProductAttributeValueService productAttributeValueService) {

        this.productRepository = ProductRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.taxRepository = taxRepository;
        this.ProductPromotionSharedService = ProductPromotionSharedService;
        this.productMapper = productMapper;
        this.taxService = taxService;
        this.ProductDiscountPriceCalculator = ProductDiscountPriceCalculator;
        this.mediaService = mediaService;
        this.localeContext = localeContext;
        this.productAttributeService = productAttributeService;
        this.productAttributeValueService = productAttributeValueService;
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        logger.debug("Creating product: {}", productDTO);

        checkDuplicateBarCode(productDTO);

        Product product = productMapper.toEntity(productDTO);

        Product savedProduct = productRepository.save(product);

        manageProductTaxesIfPresent(productDTO, savedProduct);

        manageVariantsAndAttributes(productDTO, savedProduct);

        savedProduct = productRepository.save(savedProduct);

        return productMapper.toDto(savedProduct);
    }





    @Override
    public PaginatedResponseDTO<ProductDTO> searchProductsByQuery(
            ProductFilterByCategoryAndQueryRequestDTO filterRequest,
            Pageable pageable
    ) {
        String query = filterRequest.getQuery();
        UUID[] categoryIds = filterRequest.getCategoriesIds() != null
                ? filterRequest.getCategoriesIds().toArray(new UUID[0])
                : null;
        Boolean inStock = filterRequest.getInStock() != null ? filterRequest.getInStock() : null;

        Page<Product> ProductPage = productRepository.searchByQueryAndFilters(
                query,
                categoryIds,
                inStock,
                localeContext.getLocale(),
                pageable
        );

        List<ProductDTO> ProductDTOs = ProductPage.getContent().stream()
                .map(productMapper::toDto)
                .toList();

        return new PaginatedResponseDTO<>(ProductDTOs, ProductPage.getTotalElements());
    }


    @Override
    public PaginatedResponseDTO<ProductDTO> searchProductsByQuery(String query, Pageable pageable) {
        logger.debug("Searching menu items by query: {}", query);
        Page<Product> ProductPage = productRepository.findAll(pageable);
        List<Product> products = ProductPage.getContent();
        List<ProductDTO> ProductDTOs = products.stream()
                .map(product -> {
                    ProductDTO dto = productMapper.toDto(product);
                    return verifyProductIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, dto);
                })
                .toList();
        return new PaginatedResponseDTO<>(ProductDTOs, ProductPage.getTotalElements());


    }

    public ProductDTO getProductById(UUID id) {

        logger.debug("Getting menu item by ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
        Integer salesCount = orderRepository.sumQuantityByProductId(product.getId());
        if (salesCount == null) {
            salesCount = 0;
        }
        long reviewCount = reviewRepository.countByProductId(product.getId());
        Double averageRating = reviewRepository.findAverageRatingByProductId(product.getId());
        if (averageRating == null) {
            averageRating = 0.0;
        }
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        ProductDTO productDTO = productMapper.toProductDtoWithMoreInformation(product, salesCount, reviewCount, averageRating);

        //  return verifyProductIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, productDTO);
        return productDTO;

    }
 @Override
    public PaginatedResponseDTO<ProductDTO> getAllProducts(
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
        Page<Product> ProductPage;
        if (categoryId != null) {
            ProductPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            ProductPage = productRepository.findAll(pageable);
        }
        return new PaginatedResponseDTO<>(ProductPage.getContent().stream().map(productMapper::toDto).toList(), ProductPage.getTotalElements());
    }

    @Transactional
    public ResponseEntity<ProductDTO> updateProduct(UUID id, ProductDTO productDTO) {

        logger.debug("Updating menu item with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));

        if (productDTO.getTax() != null) {
            Tax existingTax = existingProduct.getTax();
            BigDecimal newPrice = productDTO.getPrice();
            double newTaxRate = productDTO.getTax().getRate();
            double existingTaxRate = (existingTax != null) ? existingTax.getRate() : 0;
            BigDecimal existingPriceTTC = existingProduct.getPrice();
            Tax tax = existingTax != null ? existingTax : new Tax();
            tax.setRate(newTaxRate);
            tax.setName(productDTO.getTax().getName());
            tax = taxRepository.save(tax);
            existingProduct.setTax(tax);
        }

        productMapper.updateProductFromDto(productDTO, existingProduct);
        Product savedProduct = productRepository.save(existingProduct);
        ProductDTO responseDTO = modelMapper.map(savedProduct, ProductDTO.class);

        return ResponseEntity.ok(responseDTO);
    }


    @Override
    public ProductDTO decrementProductQuantity(UUID ProductId, int quantity) {

        Product product = productRepository.findById(ProductId).orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + ProductId));
        if (product.getQuantity() < quantity) {
            throw new EntityNotFoundException("Product quantity is not enough");
        }
        product.setQuantity(product.getQuantity() - quantity);
        return productMapper.toDto(productRepository.save(product));

    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteProduct(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
        orderRepository.removeProductReferences(product.getId());

        productRepository.delete(product);
        return ResponseEntity.ok(Map.of("message", "Menu Item successfully deleted"));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteProductsByIds(List<UUID> ids) {

        List<Product> products = productRepository.findAllById(ids);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("No Products found for the given IDs");
        }
        products.forEach(product -> {
            orderRepository.removeProductReferences(product.getId());
            productRepository.delete(product);
        });
        return ResponseEntity.ok(Map.of("message", "Menu Items successfully deleted"));
    }

    @Override
    public void deleteMediaForProduct(UUID ProductId, UUID mediaId) {
        logger.debug("Deleting media {} for menu item {}", mediaId, ProductId);

        Product product = productRepository.findById(ProductId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + ProductId + " not found"));

        product.getMedias().removeIf(media -> media.getId().equals(mediaId));

        productRepository.save(product);

        mediaService.deleteMedia(mediaId);

        logger.info("Successfully deleted media {} for menu item {}", mediaId, ProductId);
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductsByIds(List<UUID> ids) {

        logger.debug("Getting menu items by IDs: {}", ids);

        List<Product> products = productRepository.findAllById(ids);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("No Products found for the given IDs");
        }
        List<ProductDTO> ProductDTOs = products.stream()
                .map(product -> {
                    Integer salesCount = orderRepository.sumQuantityByProductId(product.getId());
                    if (salesCount == null) {
                        salesCount = 0;
                    }
                    long reviewCount = reviewRepository.countByProductId(product.getId());
                    Double averageRating = reviewRepository.findAverageRatingByProductId(product.getId());
                    if (averageRating == null) {
                        averageRating = 0.0;
                    }
                    averageRating = Math.round(averageRating * 10.0) / 10.0;


                    ProductDTO productDTO = productMapper.toProductDtoWithMoreInformation(product, salesCount, reviewCount, averageRating);
                    return verifyProductIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, productDTO);
                })
                .toList();
        return ResponseEntity.ok(ProductDTOs);
    }

    @Override
    public ProductDTO findByBarCode(String barCode) {

        return productRepository.findByBarCode(barCode)
                .map(productMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for barCode"));
    }

    private double roundToScale(double value, int scale) {

        if (scale < 0) throw new IllegalArgumentException("Scale must be a positive integer");

        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }

    @Override
    public List<ProductDTO> saveProducts(List<Product> products) {

        return productRepository.saveAll(products).stream().map(productMapper::toDto).toList();
    }

    @Override
    public ProductDTO save(Product product) {

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public Product findProductById(UUID id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
    }

    @Override
    public BigDecimal findProductDiscountedPrice(UUID ProductId) {

        Product product = productRepository.findById(ProductId).orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + ProductId));
        return ProductDiscountPriceCalculator.calculateDiscountedPrice(product);
    }

    @Override
    public List<Product> findByPromotion(Promotion promotion) {

        return productRepository.findAllByPromotionsContaining(promotion);
    }



    @Override
    public List<Product> findByCategory(Category category) {

        return productRepository.findAllByCategoriesContaining(category);
    }





    private void checkDuplicateBarCode(ProductDTO productDTO) {
        if (StringUtils.hasText(productDTO.getBarCode()) && isProductExistByBarCode(productDTO.getBarCode())) {
            throw new DuplicateProductException(
                    "Product with bar code " + productDTO.getBarCode() + " already exists"
            );
        }
    }

    private void manageProductTaxesIfPresent(ProductDTO productDTO, Product product) {
        if (productDTO.getTax() != null) {
            manageProductTaxes(productDTO, product);
        }
    }

    private void manageProductTaxes(ProductDTO productDTO, Product savedProduct) {
        Tax tax = this.taxService.createTax(productDTO);
        savedProduct.setTax(tax);


    }


    private boolean isProductExistByBarCode(String barCode) {

        return productRepository.findByBarCode(barCode).isPresent();
    }


    private void manageVariantsAndAttributes(ProductDTO productDTO, Product savedProduct) {
        if (ObjectUtils.isEmpty(productDTO.getVariants())) return;

        for (ProductVariantDTO variantDTO : productDTO.getVariants()) {
            ProductVariant variant = createVariant(savedProduct, variantDTO); // sets product link

            for (VariantOptionDTO optionDTO : variantDTO.getOptions()) {
                ProductAttribute attribute = productAttributeService.findOrCreateAttribute(savedProduct, optionDTO.getAttributeName());
                ProductAttributeValue value = productAttributeValueService.findOrCreateValue(attribute, optionDTO.getValue());

                VariantAttribute variantAttribute = new VariantAttribute();
                variantAttribute.setVariant(variant);
                variantAttribute.setAttributeValue(value);

                variant.getAttributes().add(variantAttribute);
            }

            savedProduct.getVariants().add(variant);
        }
    }

    private ProductVariant createVariant(Product product, ProductVariantDTO variantDTO) {
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(variantDTO.getSku());
        variant.setPrice(variantDTO.getPrice());
        variant.setQuantity(variantDTO.getQuantity());
        return variant;
    }


    private ProductDTO verifyProductIsPromotedForCurrentDayAndCalculateDiscountedPrice(Product product, ProductDTO productDTO) {

        boolean hasActivePromotion = ProductPromotionSharedService.isProductHasActivePromotionInCurrentDay(product.getId());
        productDTO.setPromoted(hasActivePromotion);

        BigDecimal discountedPrice = product.getPrice();

        if (hasActivePromotion) {
            discountedPrice = ProductDiscountPriceCalculator.calculateDiscountedPrice(product);
        }
        productDTO.setDiscountedPrice(discountedPrice);
        return productDTO;
    }

    private boolean isPromotionDiscountTypeByPercentage(PercentageDiscountPromotion percentageDiscountPromotion) {

        return !ObjectUtils.isEmpty(percentageDiscountPromotion) && percentageDiscountPromotion.getDiscountType().equals(DiscountType.BY_PERCENTAGE);
    }

    private boolean isPromotionDiscountTypeByAmount(PercentageDiscountPromotion percentageDiscountPromotion) {

        return !ObjectUtils.isEmpty(percentageDiscountPromotion) && percentageDiscountPromotion.getDiscountType().equals(DiscountType.BY_AMOUNT);
    }



    /*
    todo  : commented code that exsits in the product find all methode  :
     */

    //        List<ProductDTO> products = ProductPage.stream()
//                .map(product -> {
//                    Integer salesCount = orderRepository.sumQuantityByProductId(product.getId());
//                    if (salesCount == null) {
//                        salesCount = 0;
//                    }
//
//                    long reviewCount = reviewRepository.countByProductId(product.getId());
//                    Double averageRating = reviewRepository.findAverageRatingByProductId(product.getId());
//                    if (averageRating == null) {
//                        averageRating = 0.0;
//                    }
//                    averageRating = Math.round(averageRating * 10.0) / 10.0;
//                    ProductDTO productDTO = productMapper.toProductDtoWithMoreInformation(
//                            product, salesCount, reviewCount, averageRating
//                    );
//
//                    return verifyProductIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, productDTO);
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



}


