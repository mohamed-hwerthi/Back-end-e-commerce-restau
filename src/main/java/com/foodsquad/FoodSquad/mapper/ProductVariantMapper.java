package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ProductVariantDTO;
import com.foodsquad.FoodSquad.model.dto.VariantAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.ProductVariant;
import com.foodsquad.FoodSquad.model.entity.VariantAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    ProductVariantMapper INSTANCE = Mappers.getMapper(ProductVariantMapper.class);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "attributes", target = "attributes", qualifiedByName = "mapAttributes")
    ProductVariantDTO toDto(ProductVariant variant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "attributes", target = "attributes", qualifiedByName = "mapAttributeEntities")
    ProductVariant toEntity(ProductVariantDTO dto);

    @Named("mapAttributes")
    default List<VariantAttributeDTO> mapAttributes(List<VariantAttribute> attributes) {
        if (attributes == null) {
            return null;
        }
        return attributes.stream()
                .map(attr -> {
                    VariantAttributeDTO dto = new VariantAttributeDTO();
                    return dto;
                })
                .toList();
    }

    @Named("mapAttributeEntities")
    default List<VariantAttribute> mapAttributeEntities(List<VariantAttributeDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(dto -> {
                    VariantAttribute attr = new VariantAttribute() ;
                    return attr;
                })
                .toList();
    }
}
