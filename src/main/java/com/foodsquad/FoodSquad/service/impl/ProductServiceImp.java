package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.dto.ProductOptionDTO;
import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;
import com.foodsquad.FoodSquad.exception.DuplicateProductException;
import com.foodsquad.FoodSquad.mapper.CustomAttributeMapper;
import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.mapper.ProductOptionGroupMapper;
import com.foodsquad.FoodSquad.model.dto.*;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.service.declaration.*;
import com.foodsquad.FoodSquad.service.helpers.ProductDiscountPriceCalculator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;
import java.util.stream.IntStream;

import static org.springframework.data.util.ClassUtils.ifPresent;

@Slf4j
@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final ReviewRepository reviewRepository;

    private final ProductPromotionSharedService ProductPromotionSharedService;

    private final ProductMapper productMapper;

    private final TaxService taxService;

    private final ProductDiscountPriceCalculator ProductDiscountPriceCalculator;

    private final MediaService mediaService;

    private final LocaleContext localeContext;

    private final ProductAttributeService productAttributeService;

    private final ProductAttributeValueService productAttributeValueService;

    private final ProductOptionGroupMapper supplementGroupMapper;

    private final CustomAttributeMapper customAttributeMapper;



    public ProductServiceImp(ProductRepository ProductRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, @Lazy ProductPromotionSharedService ProductPromotionSharedService, ProductMapper productMapper, TaxService taxService, ProductDiscountPriceCalculator ProductDiscountPriceCalculator, MediaService mediaService, LocaleContext localeContext, ProductAttributeService productAttributeService, ProductAttributeValueService productAttributeValueService, ProductOptionGroupMapper supplementGroupMapper, CustomAttributeMapper customAttributeMapper) {

        this.productRepository = ProductRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.ProductPromotionSharedService = ProductPromotionSharedService;
        this.productMapper = productMapper;
        this.taxService = taxService;
        this.ProductDiscountPriceCalculator = ProductDiscountPriceCalculator;
        this.mediaService = mediaService;
        this.localeContext = localeContext;
        this.productAttributeService = productAttributeService;
        this.productAttributeValueService = productAttributeValueService;
        this.supplementGroupMapper = supplementGroupMapper;
        this.customAttributeMapper = customAttributeMapper;
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.debug("Creating product: {}", productDTO);

        checkDuplicateBarCode(productDTO);

        Product product = productMapper.toEntity(productDTO);

        Product savedProduct = productRepository.save(product);

        manageProductTaxesIfPresent(productDTO, savedProduct);

        manageProductOptionGroups(productDTO, savedProduct);

        manageVariantsAndAttributes(productDTO, savedProduct);

        manageCustomAttributes(productDTO, savedProduct);


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
        log.debug("Searching menu items by query: {}", query);
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

        log.debug("Getting menu item by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
        return productMapper.toDto(product);
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
        log.debug("Getting all menu items with some filters");

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
   @Override
    public ResponseEntity<ProductDTO> updateProduct(UUID id, ProductDTO productDTO) {
        log.debug("Updating menu item with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));

        productMapper.updateProductFromDto(productDTO, existingProduct);

        manageVariantsOnUpdate(productDTO, existingProduct) ;
        manageProductOptionGroupsOnUpdate(productDTO , existingProduct);

        List<CustomAttribute> dtoAttributes =customAttributeMapper.toEntityList(productDTO.getCustomAttributes());
        manageProductCustomAttributesOnUpdate(existingProduct, dtoAttributes);


        Product savedProduct = productRepository.save(existingProduct);

        ProductDTO responseDTO = productMapper.toDto(savedProduct);

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
    @Override
    public ResponseEntity<Map<String, String>> deleteProduct(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + id));
        orderRepository.removeProductReferences(product.getId());

        productRepository.delete(product);
        return ResponseEntity.ok(Map.of("message", "Menu Item successfully deleted"));
    }

    @Transactional
    @Override
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
        log.debug("Deleting media {} for menu item {}", mediaId, ProductId);

        Product product = productRepository.findById(ProductId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + ProductId + " not found"));

        product.getMedias().removeIf(media -> media.getId().equals(mediaId));


        productRepository.save(product);

        mediaService.deleteMedia(mediaId);

        log.info("Successfully deleted media {} for menu item {}", mediaId, ProductId);
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductsByIds(List<UUID> ids) {

        log.debug("Getting menu items by IDs: {}", ids);

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
    @Override
    public List<ProductDTO> getAllProductOptions() {
        log.debug("Fetching all products that are marked as options");

        List<Product> optionProducts = productRepository.findByIsOptionTrue();

        if (optionProducts.isEmpty()) {
            log.info("No option products found in the database");
        } else {
            log.info("Found {} option products", optionProducts.size());
        }

        return optionProducts.stream()
                .map(productMapper::toDto)
                .toList();
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


    private void manageVariantsAndAttributes(ProductDTO productDTO, Product parentProduct) {
        if (ObjectUtils.isEmpty(productDTO.getVariants())) return;

        for (VariantDTO variantDTO : productDTO.getVariants()) {
            if (!ObjectUtils.isEmpty(variantDTO.getOptions())) {
                for (VariantOptionDTO optionDTO : variantDTO.getOptions()) {
                    ProductAttribute attribute = productAttributeService.findOrCreateAttribute(
                            parentProduct,
                            variantDTO.getAttributeName()
                    );

                    ProductAttributeValue value = productAttributeValueService.findOrCreateValue(
                            attribute,
                            optionDTO.getValue()
                    );

                    Product variant = buildVariant(parentProduct, optionDTO);
                    variant.getVariantAttributes().add(value);

                    parentProduct.getVariants().add(variant);
                }
            }
        }
    }

    private Product buildVariant(Product parentProduct, VariantOptionDTO optionDTO) {
        Product variant = new Product();
        variant.setParent(parentProduct);
        variant.setVariant(true);
        variant.setSku(getOrGenerateSku(optionDTO.getSku()));
        variant.setPrice(getOrDefaultPrice(optionDTO.getPrice()));
        variant.setTitle(parentProduct.getTitle());
        variant.setDescription(parentProduct.getDescription());
        variant.setQuantity(getOrDefaultQuantity(optionDTO.getQuantity()));
        variant.setPurchasePrice(parentProduct.getPurchasePrice());
        variant.setLowStockThreshold(parentProduct.getLowStockThreshold());
        variant.setTax(parentProduct.getTax());
        return variant;
    }


    private String getOrGenerateSku(String sku) {
        return sku != null ? sku : UUID.randomUUID().toString();
    }

    private BigDecimal getOrDefaultPrice(BigDecimal price) {
        return price != null ? price : BigDecimal.ZERO;
    }

    private int getOrDefaultQuantity(String quantity) {
        try {
            return quantity != null ? Integer.parseInt(quantity) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
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



    private void manageVariantsOnUpdate(ProductDTO productDTO, Product existingProduct) {
        existingProduct.getVariants().removeIf(existingVariant ->
                productDTO.getVariants().stream()
                        .flatMap(v -> v.getOptions().stream())
                        .noneMatch(opt -> opt.getProductVariantId() != null &&
                                opt.getProductVariantId().equals(existingVariant.getId()))
        );

        removeDeletedAttributes(productDTO, existingProduct);
        for (VariantDTO variantDTO : productDTO.getVariants()) {
            ProductAttribute attribute;
            if (variantDTO.getAttributeId() != null) {
                attribute = findExistingAttribute(existingProduct, variantDTO);
                updateAttributeNameIfChanged(attribute, variantDTO);
            } else {
                attribute = productAttributeService.findOrCreateAttribute(existingProduct, variantDTO.getAttributeName());
                existingProduct.getAttributes().add(attribute);
            }
            for (VariantOptionDTO optionDTO : variantDTO.getOptions()) {
                if (optionDTO.getProductVariantId() != null) {
                    existingProduct.getVariants().stream()
                            .filter(v -> v.getId().equals(optionDTO.getProductVariantId()))
                            .findFirst()
                            .ifPresent(variant -> updateExistingVariant(variant, optionDTO, attribute));
                } else {
                    Product newVariant = buildVariant(existingProduct, optionDTO);
                    ProductAttributeValue value = productAttributeValueService.findOrCreateValue(attribute, optionDTO.getValue());
                    newVariant.getVariantAttributes().add(value);
                    existingProduct.getVariants().add(newVariant);
                }
            }
        }
    }private void updateExistingVariant(Product variant, VariantOptionDTO optionDTO, ProductAttribute attribute) {
        variant.setSku(getOrGenerateSku(optionDTO.getSku()));
        variant.setPrice(getOrDefaultPrice(optionDTO.getPrice()));
        variant.setQuantity(getOrDefaultQuantity(optionDTO.getQuantity()));

        if (optionDTO.getValue() != null) {
            ProductAttributeValue newValue = productAttributeValueService.findOrCreateValue(attribute, optionDTO.getValue());
            variant.getVariantAttributes().clear();
            variant.getVariantAttributes().add(newValue);
        }
    }






    private void manageProductCustomAttributesOnUpdate(Product existingProduct, List<CustomAttribute> newAttributes) {
        existingProduct.getCustomAttributes()
                .removeIf(existingAttr -> newAttributes.stream()
                        .noneMatch(newAttr -> newAttr.getId() != null && newAttr.getId().equals(existingAttr.getId())));

        for (CustomAttribute newAttr : newAttributes) {
            if (newAttr.getId() != null) {
                existingProduct.getCustomAttributes().stream()
                        .filter(e -> e.getId().equals(newAttr.getId()))
                        .findFirst()
                        .ifPresent(existing -> {
                            existing.setName(newAttr.getName());
                            existing.setValue(newAttr.getValue());
                            existing.setType(newAttr.getType());
                            existing.setProduct(existingProduct);

                        });
            } else {
                newAttr.setProduct(existingProduct);
                existingProduct.getCustomAttributes().add(newAttr);
            }
        }
    }




    private void removeDeletedVariants(ProductDTO productDTO, Product existingProduct) {
        existingProduct.getVariants().removeIf(existingVariant ->
                productDTO.getVariants().stream()
                        .flatMap(v -> v.getOptions().stream())
                        .noneMatch(opt -> opt.getProductVariantId() != null &&
                                opt.getProductVariantId().equals(existingVariant.getId()))
        );
    }

    private void removeDeletedAttributes(ProductDTO productDTO, Product existingProduct) {
        List<UUID> dtoAttributeIds = productDTO.getVariants().stream()
                .map(VariantDTO::getAttributeId)
                .filter(Objects::nonNull)
                .toList();

        existingProduct.getAttributes().removeIf(attr -> !dtoAttributeIds.contains(attr.getId()));
    }





    private ProductAttribute findExistingAttribute(Product product, VariantDTO variantDTO) {
        return product.getAttributes().stream()
                .filter(a -> a.getId().equals(variantDTO.getAttributeId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Attribute not found"));
    }

    private void updateAttributeNameIfChanged(ProductAttribute attribute, VariantDTO variantDTO) {
        if (!attribute.getName().equals(variantDTO.getAttributeName())) {
            attribute.setName(variantDTO.getAttributeName());
            productAttributeService.updateProductAttributeName(attribute.getId() , variantDTO.getAttributeName());
        }
    }


    private void manageProductOptionGroups(ProductDTO productDTO, Product product) {
        product.getProductOptionGroups().clear();
        if (ObjectUtils.isEmpty(productDTO.getProductOptionGroups())) {
            return;
        }

        List<ProductOptionGroup> groups = productDTO.getProductOptionGroups().stream().map(productOptionGroupDTO -> {
                    ProductOptionGroup group = supplementGroupMapper.toEntity(productOptionGroupDTO);
                    group.setProduct(product);

                    if (!ObjectUtils.isEmpty(group.getProductOptions())) {
                        IntStream.range(0, group.getProductOptions().size())
                                .forEach(j -> {
                                    ProductOption option = group.getProductOptions().get(j);
                                    ProductOptionDTO optionDTO = productOptionGroupDTO.getProductOptions().get(j);

                                    option.setProductOptionGroup(group);

                                    if (StringUtils.hasText(optionDTO.getLinkedProductId().toString())) {
                                        productRepository.findById(optionDTO.getLinkedProductId())
                                                .ifPresent(option::setLinkedProduct);
                                    }
                                });
                    }
                    return group;
                })
                .toList();

        product.getProductOptionGroups().addAll(groups);
    }

    private void manageProductOptionGroupsOnUpdate(ProductDTO productDTO, Product existingProduct) {
        // Remove groups not present in DTO
        existingProduct.getProductOptionGroups().removeIf(existingGroup ->
                productDTO.getProductOptionGroups().stream()
                        .noneMatch(dtoGroup -> dtoGroup.getId() != null && dtoGroup.getId().equals(existingGroup.getId()))
        );

        // Iterate through DTO groups
        for (ProductOptionGroupDTO dtoGroup : productDTO.getProductOptionGroups()) {
            if (dtoGroup.getId() != null) {
                // Existing group -> update
                existingProduct.getProductOptionGroups().stream()
                        .filter(g -> g.getId().equals(dtoGroup.getId()))
                        .findFirst()
                        .ifPresent(existingGroup -> {
                            existingGroup.setName(dtoGroup.getName());
                            existingGroup.setProduct(existingProduct);
                            manageProductOptionsOnUpdate(dtoGroup, existingGroup, existingProduct);
                        });
            } else {
                // New group -> add
                ProductOptionGroup newGroup = supplementGroupMapper.toEntity(dtoGroup);
                newGroup.setProduct(existingProduct);

                // Ensure options are linked
                for (ProductOption option : newGroup.getProductOptions()) {
                    option.setProductOptionGroup(newGroup);
                    if (option.getLinkedProduct() != null) {
                        productRepository.findById(option.getLinkedProduct().getId())
                                .ifPresent(option::setLinkedProduct);
                    }
                }

                existingProduct.getProductOptionGroups().add(newGroup);
            }
        }
    }

    private void manageProductOptionsOnUpdate(ProductOptionGroupDTO dtoGroup, ProductOptionGroup existingGroup, Product existingProduct) {
        existingGroup.getProductOptions().removeIf(existingOption ->
                dtoGroup.getProductOptions().stream()
                        .noneMatch(dtoOpt -> dtoOpt.getId() != null && dtoOpt.getId().equals(existingOption.getId()))
        );

        for (ProductOptionDTO dtoOpt : dtoGroup.getProductOptions()) {
            if (dtoOpt.getId() != null) {
                existingGroup.getProductOptions().stream()
                        .filter(opt -> opt.getId().equals(dtoOpt.getId()))
                        .findFirst()
                        .ifPresent(existingOption -> {
                            existingOption.setOverridePrice(dtoOpt.getOverridePrice());

                            if (dtoOpt.getLinkedProductId() != null) {
                                productRepository.findById(dtoOpt.getLinkedProductId())
                                        .ifPresent(existingOption::setLinkedProduct);
                            } else {
                                existingOption.setLinkedProduct(null);
                            }
                            existingOption.setProductOptionGroup(existingGroup);
                        });
            } else {
                ProductOption.ProductOptionBuilder builder = ProductOption.builder()
                        .overridePrice(dtoOpt.getOverridePrice())
                        .productOptionGroup(existingGroup);

                if (dtoOpt.getLinkedProductId() != null) {
                    productRepository.findById(dtoOpt.getLinkedProductId())
                            .ifPresent(builder::linkedProduct);
                }

                existingGroup.getProductOptions().add(builder.build());
            }
        }
    }



    private void manageCustomAttributes(ProductDTO productDTO, Product product) {
        if (productDTO.getCustomAttributes() != null && !productDTO.getCustomAttributes().isEmpty()) {
            List<CustomAttribute> attributes = new ArrayList<>();
            for (var dto : productDTO.getCustomAttributes()) {
                CustomAttribute attribute = customAttributeMapper.toEntity(dto);
                attribute.setProduct(product);
                attributes.add(attribute);
            }
            product.setCustomAttributes(attributes);
        }
    }


    private boolean isPromotionDiscountTypeByPercentage(PercentageDiscountPromotion percentageDiscountPromotion) {

        return !ObjectUtils.isEmpty(percentageDiscountPromotion) && percentageDiscountPromotion.getDiscountType().equals(DiscountType.BY_PERCENTAGE);
    }

    private boolean isPromotionDiscountTypeByAmount(PercentageDiscountPromotion percentageDiscountPromotion) {

        return !ObjectUtils.isEmpty(percentageDiscountPromotion) && percentageDiscountPromotion.getDiscountType().equals(DiscountType.BY_AMOUNT);
    }


}







