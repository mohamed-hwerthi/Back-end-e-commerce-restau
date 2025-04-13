package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.TimbreDTO;
import com.foodsquad.FoodSquad.model.entity.Timbre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper( componentModel = MappingConstants.ComponentModel.SPRING)

public interface TimbreMapper {
    Timbre toEntity(TimbreDTO timbreDTO)  ;
    TimbreDTO toDto(Timbre timbre);
}
