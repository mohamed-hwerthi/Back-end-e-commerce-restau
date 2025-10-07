package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.dto.ProductOptionDTO;
import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;
import com.foodsquad.FoodSquad.exception.DuplicateProductException;
import com.foodsquad.FoodSquad.mapper.CustomAttributeMapper;
import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.mapper.ProductOptionGroupMapper;
import com.foodsquad.FoodSquad.model.dto.*;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductListDTO;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.service.admin.dec.*;
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
        log.debug("Creating product with data: {}", productDTO);

        checkDuplicateBarCode(productDTO);

        Product product = productMapper.toEntity(productDTO);
        log.debug("Mapped ProductDTO to Product entity");

        Product savedProduct = productRepository.save(product);
        log.debug("Saved product with ID: {}", savedProduct.getId());

        manageProductTaxesIfPresent(productDTO, savedProduct);
        manageProductOptionGroups(productDTO, savedProduct);
        manageVariantsAndAttributes(productDTO, savedProduct);
        manageCustomAttributes(productDTO, savedProduct);

        savedProduct = productRepository.save(savedProduct);
        log.debug("Final product saved with ID: {}", savedProduct.getId());

        ProductDTO responseDTO = productMapper.toDto(savedProduct);
        log.debug("Returning ProductDTO: {}", responseDTO);
        return responseDTO;
    }


    @Override
    public PaginatedResponseDTO<ProductDTO> searchProductsByQuery(
            ProductFilterByCategoryAndQueryRequestDTO filterRequest,
            Pageable pageable
    ) {
        log.debug("Searching products with filter request: {}", filterRequest);

        String query = filterRequest.getQuery();
        UUID[] categoryIds = filterRequest.getCategoriesIds() != null
                ? filterRequest.getCategoriesIds().toArray(new UUID[0])
                : null;
        Boolean inStock = filterRequest.getInStock();

        Page<Product> productPage = productRepository.searchByQueryAndFilters(
                query,
                categoryIds,
                inStock,
                localeContext.getLocale(),
                pageable
        );

        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(productMapper::toDto)
                .toList();

        log.debug("Found {} products matching the filters", productDTOs.size());
        return new PaginatedResponseDTO<>(productDTOs, productPage.getTotalElements());
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> searchProductsByQuery(String query, Pageable pageable) {
        log.debug("Searching products by query: {}", query);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(product -> {
                    ProductDTO dto = productMapper.toDto(product);
                    return verifyProductIsPromotedForCurrentDayAndCalculateDiscountedPrice(product, dto);
                })
                .toList();

        log.debug("Found {} products for query '{}'", productDTOs.size(), query);
        return new PaginatedResponseDTO<>(productDTOs, productPage.getTotalElements());
    }


    @Override
    public ProductDTO getProductById(UUID id) {
        log.debug("Fetching product by ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new EntityNotFoundException("Product not found for ID: " + id);
                });

        ProductDTO productDTO = productMapper.toDto(product);
        log.debug("Product found: {}", productDTO);

        return productDTO;
    }


    @Override
    public PaginatedResponseDTO<ClientProductListDTO> getAllProducts(
            int page,
            int limit,
            boolean desc,
            UUID categoryId,
            String priceSortDirection
    ) {
        log.debug("Fetching products with filters - page: {}, limit: {}, sortBy: {}, desc: {}, categoryId: {}, isDefault: {}, priceSortDirection: {}",
                page, limit, desc, categoryId, priceSortDirection);

        Sort.Direction direction = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(direction));

        Page<Product> productPage;

        if (categoryId != null) {
            log.debug("Filtering products by category ID: {}", categoryId);
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            log.debug("Fetching all products without category filter");
            productPage = productRepository.findAll(pageable);
        }

        List<ClientProductListDTO> productDTOs = productPage.getContent().stream()
                .map(productMapper::toClientProductListDTO)
                .toList();

        log.debug("Fetched {} products, total elements: {}", productDTOs.size(), productPage.getTotalElements());

        return new PaginatedResponseDTO<>(productDTOs, productPage.getTotalElements());
    }


    @Override
    public ResponseEntity<ProductDTO> updateProduct(UUID id, ProductDTO productDTO) {
        log.debug("Starting update for Product with ID: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found for ID: {}", id);
                    return new EntityNotFoundException("Product not found for ID: " + id);
                });
        log.debug("Existing product fetched: {}", existingProduct.getId());

        log.debug("Updating product fields from DTO");
        productMapper.updateProductFromDto(productDTO, existingProduct);

        log.debug("Updating product variants");
        manageVariantsOnUpdate(productDTO, existingProduct);

        log.debug("Updating product option groups");
        manageProductOptionGroupsOnUpdate(productDTO, existingProduct);

        log.debug("Updating custom attributes");
        List<CustomAttribute> dtoAttributes = customAttributeMapper.toEntityList(productDTO.getCustomAttributes());
        manageProductCustomAttributesOnUpdate(existingProduct, dtoAttributes);

        log.debug("Saving updated product");
        Product savedProduct = productRepository.save(existingProduct);

        ProductDTO responseDTO = productMapper.toDto(savedProduct);
        log.debug("Product updated successfully: {}", responseDTO.getId());

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

        productDTO.getVariants().stream()
                .filter(variantDTO -> !ObjectUtils.isEmpty(variantDTO.getOptions()))
                .forEach(variantDTO -> processVariantDTO(variantDTO, parentProduct));
    }

    private void processVariantDTO(VariantDTO variantDTO, Product parentProduct) {
        ProductAttribute attribute = getOrCreateAttributeOnSave(parentProduct, variantDTO);

        variantDTO.getOptions().stream()
                .map(optionDTO -> buildVariantWithAttribute(parentProduct, optionDTO, attribute))
                .forEach(parentProduct.getVariants()::add);
    }

    private ProductAttribute getOrCreateAttributeOnSave(Product parentProduct, VariantDTO variantDTO) {
        return productAttributeService.findOrCreateAttribute(parentProduct, variantDTO.getAttributeName());
    }

    private Product buildVariantWithAttribute(Product parentProduct, VariantOptionDTO optionDTO, ProductAttribute attribute) {
        Product variant = buildVariant(parentProduct, optionDTO);
        ProductAttributeValue value = productAttributeValueService.findOrCreateValue(attribute, optionDTO.getValue());
        variant.getVariantAttributes().add(value);
        return variant;
    }

    private Product buildVariant(Product parentProduct, VariantOptionDTO optionDTO) {
        return Product.builder()
                .parent(parentProduct)
                .isVariant(true)
                .sku(getOrGenerateSku(optionDTO.getSku()))
                .price(getOrDefaultPrice(optionDTO.getPrice()))
                .title(parentProduct.getTitle())
                .description(parentProduct.getDescription())
                .quantity(getOrDefaultQuantity(optionDTO.getQuantity()))
                .purchasePrice(parentProduct.getPurchasePrice())
                .lowStockThreshold(parentProduct.getLowStockThreshold())
                .tax(parentProduct.getTax())
                .variantAttributes(new HashSet<>())
                .build();
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
        removeDeletedVariants(productDTO, existingProduct);
        removeDeletedAttributes(productDTO, existingProduct);

        for (VariantDTO variantDTO : productDTO.getVariants()) {
            ProductAttribute attribute = getOrCreateAttribute(existingProduct, variantDTO);
            processVariantOptions(existingProduct, variantDTO, attribute);
        }
    }


    private ProductAttribute getOrCreateAttribute(Product existingProduct, VariantDTO variantDTO) {
        if (variantDTO.getAttributeId() != null) {
            ProductAttribute attribute = findExistingAttribute(existingProduct, variantDTO);
            updateAttributeNameIfChanged(attribute, variantDTO);
            return attribute;
        } else {
            ProductAttribute attribute = productAttributeService.findOrCreateAttribute(existingProduct, variantDTO.getAttributeName());
            existingProduct.getAttributes().add(attribute);
            return attribute;
        }
    }

    private void processVariantOptions(Product existingProduct, VariantDTO variantDTO, ProductAttribute attribute) {
        for (VariantOptionDTO optionDTO : variantDTO.getOptions()) {
            if (optionDTO.getProductVariantId() != null) {
                updateExistingVariantOption(existingProduct, optionDTO, attribute);
            } else {
                addNewVariant(existingProduct, optionDTO, attribute);
            }
        }
    }

    private void updateExistingVariantOption(Product existingProduct, VariantOptionDTO optionDTO, ProductAttribute attribute) {
        existingProduct.getVariants().stream()
                .filter(v -> v.getId().equals(optionDTO.getProductVariantId()))
                .findFirst()
                .ifPresent(variant -> updateExistingVariant(variant, optionDTO, attribute));
    }

    private void addNewVariant(Product existingProduct, VariantOptionDTO optionDTO, ProductAttribute attribute) {
        Product newVariant = buildVariant(existingProduct, optionDTO);
        ProductAttributeValue value = productAttributeValueService.findOrCreateValue(attribute, optionDTO.getValue());
        newVariant.getVariantAttributes().add(value);
        existingProduct.getVariants().add(newVariant);
    }


    private void updateExistingVariant(Product variant, VariantOptionDTO optionDTO, ProductAttribute attribute) {
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
        removeDeletedCustomAttributes(existingProduct, newAttributes);
        updateOrAddCustomAttributes(existingProduct, newAttributes);
    }

    private void removeDeletedCustomAttributes(Product existingProduct, List<CustomAttribute> newAttributes) {
        existingProduct.getCustomAttributes().removeIf(existingAttr ->
                newAttributes.stream()
                        .noneMatch(newAttr -> newAttr.getId() != null && newAttr.getId().equals(existingAttr.getId()))
        );
    }

    private void updateOrAddCustomAttributes(Product existingProduct, List<CustomAttribute> newAttributes) {
        for (CustomAttribute newAttr : newAttributes) {
            if (newAttr.getId() != null) {
                updateExistingCustomAttribute(existingProduct, newAttr);
            } else {
                addNewCustomAttribute(existingProduct, newAttr);
            }
        }
    }

    private void updateExistingCustomAttribute(Product existingProduct, CustomAttribute newAttr) {
        existingProduct.getCustomAttributes().stream()
                .filter(e -> e.getId().equals(newAttr.getId()))
                .findFirst()
                .ifPresent(existing -> {
                    existing.setName(newAttr.getName());
                    existing.setValue(newAttr.getValue());
                    existing.setType(newAttr.getType());
                    existing.setProduct(existingProduct);
                });
    }

    private void addNewCustomAttribute(Product existingProduct, CustomAttribute newAttr) {
        newAttr.setProduct(existingProduct);
        existingProduct.getCustomAttributes().add(newAttr);
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
            productAttributeService.updateProductAttributeName(attribute.getId(), variantDTO.getAttributeName());
        }
    }


    private void manageProductOptionGroups(ProductDTO productDTO, Product product) {


        if (ObjectUtils.isEmpty(productDTO.getProductOptionGroups())) {
            return;
        }

        List<ProductOptionGroup> groups = productDTO.getProductOptionGroups().stream()
                .map(dtoGroup -> mapToEntityGroup(dtoGroup, product))
                .toList();

        product.getProductOptionGroups().addAll(groups);
    }

    private ProductOptionGroup mapToEntityGroup(ProductOptionGroupDTO dtoGroup, Product product) {
        ProductOptionGroup group = supplementGroupMapper.toEntity(dtoGroup);
        group.setProduct(product);

        if (!ObjectUtils.isEmpty(group.getProductOptions())) {
            initializeProductOptions(dtoGroup, group);
        }

        return group;
    }

    private void initializeProductOptions(ProductOptionGroupDTO dtoGroup, ProductOptionGroup group) {
        IntStream.range(0, group.getProductOptions().size())
                .forEach(index -> {
                    ProductOption option = group.getProductOptions().get(index);
                    ProductOptionDTO optionDTO = dtoGroup.getProductOptions().get(index);

                    option.setProductOptionGroup(group);
                    linkProductIfPresent(option, optionDTO);
                });
    }

    private void linkProductIfPresent(ProductOption option, ProductOptionDTO optionDTO) {
        if (optionDTO.getLinkedProductId() != null) {
            productRepository.findById(optionDTO.getLinkedProductId())
                    .ifPresent(option::setLinkedProduct);
        }
    }


    private void manageProductOptionGroupsOnUpdate(ProductDTO productDTO, Product existingProduct) {
        removeDeletedGroups(productDTO, existingProduct);
        updateExistingGroups(productDTO, existingProduct);
        addNewGroups(productDTO, existingProduct);
    }

    private void removeDeletedGroups(ProductDTO productDTO, Product existingProduct) {
        existingProduct.getProductOptionGroups().removeIf(existingGroup ->
                productDTO.getProductOptionGroups().stream()
                        .noneMatch(dtoGroup -> dtoGroup.getId() != null && dtoGroup.getId().equals(existingGroup.getId()))
        );
    }

    private void updateExistingGroups(ProductDTO productDTO, Product existingProduct) {
        for (ProductOptionGroupDTO dtoGroup : productDTO.getProductOptionGroups()) {
            if (dtoGroup.getId() != null) {
                existingProduct.getProductOptionGroups().stream()
                        .filter(g -> g.getId().equals(dtoGroup.getId()))
                        .findFirst()
                        .ifPresent(existingGroup -> updateGroup(dtoGroup, existingGroup, existingProduct));
            }
        }
    }

    private void updateGroup(ProductOptionGroupDTO dtoGroup, ProductOptionGroup existingGroup, Product existingProduct) {
        existingGroup.setName(dtoGroup.getName());
        existingGroup.setProduct(existingProduct);
        manageProductOptionsOnUpdate(dtoGroup, existingGroup, existingProduct);
    }

    private void addNewGroups(ProductDTO productDTO, Product existingProduct) {
        for (ProductOptionGroupDTO dtoGroup : productDTO.getProductOptionGroups()) {
            if (dtoGroup.getId() == null) {
                ProductOptionGroup newGroup = supplementGroupMapper.toEntity(dtoGroup);
                newGroup.setProduct(existingProduct);
                initializeProductOptions(newGroup);
                existingProduct.getProductOptionGroups().add(newGroup);
            }
        }
    }

    private void initializeProductOptions(ProductOptionGroup newGroup) {
        for (ProductOption option : newGroup.getProductOptions()) {
            option.setProductOptionGroup(newGroup);
            if (option.getLinkedProduct() != null) {
                productRepository.findById(option.getLinkedProduct().getId())
                        .ifPresent(option::setLinkedProduct);
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







