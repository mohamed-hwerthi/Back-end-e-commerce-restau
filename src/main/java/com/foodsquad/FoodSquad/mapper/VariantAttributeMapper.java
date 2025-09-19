package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.VariantAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.VariantAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VariantAttributeMapper {

    VariantAttributeMapper INSTANCE = Mappers.getMapper(VariantAttributeMapper.class);

    VariantAttributeDTO toDto(VariantAttribute variantAttribute);

    VariantAttribute toEntity(VariantAttributeDTO variantAttributeDTO);
}
