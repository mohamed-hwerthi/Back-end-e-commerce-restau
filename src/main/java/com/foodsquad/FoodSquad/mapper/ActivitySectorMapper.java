package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ActivitySectorDTO;
import com.foodsquad.FoodSquad.model.entity.ActivitySector;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivitySectorMapper  extends   GenericMapper<ActivitySector, ActivitySectorDTO> {
}
