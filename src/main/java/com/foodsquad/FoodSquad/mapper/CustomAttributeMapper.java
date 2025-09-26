package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CustomAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.CustomAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomAttributeMapper {

    @Mapping(target = "product", ignore = true)
    CustomAttribute toEntity(CustomAttributeDTO dto);

    CustomAttributeDTO toDto(CustomAttribute entity);
}
