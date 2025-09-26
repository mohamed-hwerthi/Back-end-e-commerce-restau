package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.VariantDTO;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductVariant;
import com.foodsquad.FoodSquad.model.entity.VariantAttribute;
import com.foodsquad.FoodSquad.model.entity.VariantOptionDTO;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapper.class, MediaMapper.class, TaxMapper.class}
)
public interface ProductMapper {

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "supplementGroups", ignore = true)
    @Mapping(target = "customAttributes", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "variants", ignore = true)

    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", ignore = true)
    void updateProductFromDto(ProductDTO dto, @MappingTarget Product entity);

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
        return product.getVariants().stream()
                .flatMap(variant -> variant.getAttributes().stream())
                .collect(Collectors.toMap(
                        attr -> attr.getAttributeValue().getProductAttribute().getId(),
                        attr -> attr.getAttributeValue().getProductAttribute().getName(),
                        (existing, replacement) -> existing, // keep first if duplicate
                        LinkedHashMap::new
                ))
                .entrySet().stream()
                .map(entry -> new ProductAttributeDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    private List<VariantDTO> buildVariants(Product product) {
        Map<UUID, List<ProductVariant>> groupedByAttribute = product.getVariants().stream()
                .collect(Collectors.groupingBy(variant ->
                        variant.getAttributes().get(0).getAttributeValue().getProductAttribute().getId()
                ));

        return groupedByAttribute.entrySet().stream()
                .map(entry -> {
                    UUID attributeId = entry.getKey();
                    List<ProductVariant> variantsForAttribute = entry.getValue();
                    VariantDTO variantDTO = new VariantDTO();
                    variantDTO.setAttributeId(attributeId);
                    variantDTO.setAttributeName(
                            variantsForAttribute.get(0).getAttributes()
                                    .get(0).getAttributeValue().getProductAttribute().getName()
                    );
                    List<VariantOptionDTO> options = variantsForAttribute.stream()
                            .map(variant -> {
                                VariantAttribute attr = variant.getAttributes().get(0);
                                return VariantOptionDTO.builder()
                                        .id(attr.getId())
                                        .value(attr.getAttributeValue().getValue())
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

