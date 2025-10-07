package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.VariantDTO;
import com.foodsquad.FoodSquad.model.dto.client.*;
import com.foodsquad.FoodSquad.model.entity.*;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapper.class, MediaMapper.class, TaxMapper.class, CustomAttributeMapper.class, ProductOptionGroupMapper.class}
)
public interface ProductMapper {

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "productOptionGroups", ignore = true)
    @Mapping(target = "customAttributes", ignore = true)
    @Mapping(target = "isOption", source = "option")
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "variants", ignore = true)
    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "customAttributes", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductDTO dto, @MappingTarget Product entity);

    ClientProductListDTO toClientProductListDTO(Product product);

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "mediasUrls", ignore = true)
    @Mapping(target = "basePrice", source = "price")
    ClientProductDetailDTO toClientProductDetailDTO(Product product);

    default ProductDTO toProductDtoWithMoreInformation(
            Product product, int salesCount, long reviewCount, double averageRating
    ) {
        ProductDTO dto = toDto(product);
        dto.setSalesCount(salesCount);
        dto.setReviewCount(reviewCount);
        dto.setAverageRating(averageRating);
        return dto;
    }

    @AfterMapping
    default void enrichWithVariantsAndAttributes(Product product, @MappingTarget ProductDTO dto) {
        if (ObjectUtils.isEmpty(product.getVariants())) {
            return;
        }

        dto.setAvailableAttributes(buildAvailableAttributes(product));
        dto.setVariants(buildVariants(product));
    }


    @AfterMapping
    default void enrichDetailWithVariantsAndOptions(Product product, @MappingTarget ClientProductDetailDTO dto) {
        if (!ObjectUtils.isEmpty(product.getVariants())) {
            dto.setVariants(buildVariantsForClientProductDetailDTO(product));

        }
        if (!ObjectUtils.isEmpty(product.getCategories())) {
            dto.setCategoryName(product.getCategories().get(0).getName());
        }
        if (!ObjectUtils.isEmpty(product.getMedias())) {
            dto.setMediasUrls(product.getMedias().stream().map(Media::getUrl).toList());
        }
        if (!ObjectUtils.isEmpty(product.getProductOptionGroups())) {
            dto.setOptionGroups(buildOptionsForClientProductDetailDTO(product));
        }
    }

    private List<ProductAttributeDTO> buildAvailableAttributes(Product product) {
        return product.getAttributes().stream().map(
                productAttribute -> ProductAttributeDTO.builder()
                        .id(productAttribute.getId())
                        .name(productAttribute.getName())
                        .build()
        ).toList();
    }

    private List<VariantDTO> buildVariants(Product product) {
        Map<UUID, List<Product>> groupedByAttribute = product.getVariants().stream()
                .flatMap(variant -> variant.getVariantAttributes().stream()
                        .map(attrValue -> Map.entry(attrValue.getProductAttribute().getId(), variant))
                )
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        return groupedByAttribute.entrySet().stream()
                .map(entry -> {
                    UUID attributeId = entry.getKey();
                    List<Product> variantsForAttribute = entry.getValue();

                    VariantDTO variantDTO = new VariantDTO();
                    variantDTO.setAttributeId(attributeId);

                    ProductAttribute attribute = variantsForAttribute.get(0).getVariantAttributes().stream()
                            .filter(val -> val.getProductAttribute().getId().equals(attributeId))
                            .findFirst()
                            .map(ProductAttributeValue::getProductAttribute)
                            .orElseThrow();

                    variantDTO.setAttributeName(attribute.getName());
                    List<VariantOptionDTO> options = variantsForAttribute.stream()
                            .map(variant -> {
                                ProductAttributeValue attrValue = variant.getVariantAttributes().stream()
                                        .filter(val -> val.getProductAttribute().getId().equals(attributeId))
                                        .findFirst()
                                        .orElseThrow();

                                return VariantOptionDTO.builder()
                                        .id(attrValue.getId())
                                        .value(attrValue.getValue())
                                        .price(variant.getPrice())
                                        .sku(variant.getSku())
                                        .quantity(String.valueOf(variant.getQuantity()))
                                        .productVariantId(variant.getId())
                                        .variantAttributeID(attributeId)
                                        .build();
                            })
                            .toList();

                    variantDTO.setOptions(options);
                    return variantDTO;
                })
                .toList();
    }

    private List<ClientProductVariants> buildVariantsForClientProductDetailDTO(Product product) {

        Map<UUID, List<Product>> groupedByAttribute = product.getVariants().stream()
                .flatMap(variant -> variant.getVariantAttributes().stream()
                        .map(attrValue -> Map.entry(attrValue.getProductAttribute().getId(), variant))
                )
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));


        return groupedByAttribute.entrySet().stream()
                .map(entry -> {
                    UUID attributeId = entry.getKey();
                    List<Product> variantsForAttribute = entry.getValue();

                    ProductAttribute attribute = variantsForAttribute.get(0).getVariantAttributes().stream()
                            .filter(val -> val.getProductAttribute().getId().equals(attributeId))
                            .findFirst()
                            .map(ProductAttributeValue::getProductAttribute)
                            .orElseThrow();

                    List<ClientProductVariantOption> options = variantsForAttribute.stream()
                            .map(variant -> {
                                ProductAttributeValue attrValue = variant.getVariantAttributes().stream()
                                        .filter(val -> val.getProductAttribute().getId().equals(attributeId))
                                        .findFirst()
                                        .orElseThrow();


                                return ClientProductVariantOption.builder()
                                        .variantId(variant.getId())
                                        .variantValue(attrValue.getValue())
                                        .variantPrice(variant.getPrice())
                                        .build();
                            })
                            .toList();

                    return ClientProductVariants.builder()
                            .variantName(attribute.getName())
                            .options(options)
                            .build();
                })
                .toList();
    }

    private List<ClientProductOptionGroup> buildOptionsForClientProductDetailDTO(Product product) {
        if (ObjectUtils.isEmpty(product.getProductOptionGroups())) {
            return List.of();
        }

        return product.getProductOptionGroups().stream()
                .map(this::mapOptionGroup)
                .toList();
    }

    private ClientProductOptionGroup mapOptionGroup(ProductOptionGroup group) {
        ClientProductOptionGroup groupDTO = new ClientProductOptionGroup();
        groupDTO.setName(group.getName());
        groupDTO.setOptions(mapOptions(group));
        return groupDTO;
    }

    private List<ClientProductOption> mapOptions(ProductOptionGroup group) {
        if (ObjectUtils.isEmpty(group.getProductOptions())) {
            return List.of();
        }

        return group.getProductOptions().stream()
                .map(this::mapOption)
                .toList();
    }

    private ClientProductOption mapOption(ProductOption option) {
        Product linkedProduct = option.getLinkedProduct();

        return ClientProductOption.builder()
                .optionId(linkedProduct != null ? linkedProduct.getId() : null)
                .optionName(linkedProduct != null ? linkedProduct.getTitle() : null)
                .optionPrice(option.getOverridePrice())
                .build();
    }


}

