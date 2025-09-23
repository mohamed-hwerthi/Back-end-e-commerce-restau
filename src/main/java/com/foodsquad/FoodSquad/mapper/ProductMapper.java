package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.ProductVariantDTO;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductVariant;
import com.foodsquad.FoodSquad.model.entity.VariantAttribute;
import org.mapstruct.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapper.class, MediaMapper.class, TaxMapper.class}
)
public interface ProductMapper {

    @Mapping(target = "variants", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "variants", ignore = true)
    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
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
//
//    @AfterMapping
//    default void enrichWithVariantsAndAttributes(Product product, @MappingTarget ProductDTO dto) {
//        if (product.getVariants() == null || product.getVariants().isEmpty()) {
//            return;
//        }
//
//        dto.setVariants(
//                product.getVariants().stream()
//                        .map(this::mapVariantToDto)
//                        .toList()
//        );
//
//        List<ProductAttributeDTO> availableAttributes = product.getVariants().stream()
//                .flatMap(variant -> variant.getAttributes().stream())
//                .collect(Collectors.toMap(
//                        attr -> attr.getAttributeValue().getProductAttribute().getId(),
//                        attr -> attr.getAttributeValue().getProductAttribute().getName(),
//                        (existing, replacement) -> existing, // keep first if duplicate
//                        LinkedHashMap::new
//                ))
//                .entrySet().stream()
//                .map(entry -> new ProductAttributeDTO(entry.getKey(), entry.getValue()))
//                .toList();
//
//        dto.setAvailableAttributes(availableAttributes);
//    }
//
//
//    private ProductVariantDTO mapVariantToDto(ProductVariant variant) {
//        ProductVariantDTO dto = new ProductVariantDTO();
//        dto.setId(variant.getId());
//        dto.setSku(variant.getSku());
//        dto.setPrice(variant.getPrice());
//        dto.setQuantity(variant.getQuantity());
//        dto.setOptions(
//                variant.getAttributes().stream()
//                        .map(this::mapVariantOptionToDto)
//                        .toList()
//        );
//        return dto;
//    }
//
//    private VariantOptionDTO mapVariantOptionToDto(VariantAttribute attr) {
//        VariantOptionDTO dto = new VariantOptionDTO();
//        dto.setId(attr.getId());
//        dto.setValue(attr.getAttributeValue().getValue());
//        dto.setAttributeName(attr.getAttributeValue().getProductAttribute().getName());
//        return dto;
//    }
}
