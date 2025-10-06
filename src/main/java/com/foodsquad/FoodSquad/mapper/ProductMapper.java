package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.VariantDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductListDTO;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import com.foodsquad.FoodSquad.model.entity.VariantOptionDTO;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapper.class, MediaMapper.class, TaxMapper.class, CustomAttributeMapper.class, ProductOptionGroupMapper.class
        }
)
public interface ProductMapper {

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "productOptionGroups", ignore = true)
    @Mapping(target = "customAttributes", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "variants", ignore = true)
    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "tax", ignore = true)
    @Mapping(target = "customAttributes", ignore = true)
    void updateProductFromDto(ProductDTO dto, @MappingTarget Product entity);

    @Mapping(target = "variants", ignore = true)
    ClientProductListDTO toClientProductDTO(Product product);

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


}

