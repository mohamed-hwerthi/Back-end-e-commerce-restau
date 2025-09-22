package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {
    ProductAttributeMapper INSTANCE = Mappers.getMapper(ProductAttributeMapper.class);

    ProductAttributeDTO toDto(ProductAttribute productAttribute);

    ProductAttribute toEntity(ProductAttributeDTO productAttributeDTO);
}
