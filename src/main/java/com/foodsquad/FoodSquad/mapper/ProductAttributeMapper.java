package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {
    ProductAttributeMapper INSTANCE = Mappers.getMapper(ProductAttributeMapper.class);

    @Mapping(source = "product.id", target = "productId")
    ProductAttributeDTO toDto(ProductAttribute productAttribute);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "values", ignore = true)
    ProductAttribute toEntity(ProductAttributeDTO productAttributeDTO);
}
