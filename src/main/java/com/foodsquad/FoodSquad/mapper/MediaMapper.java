package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper {
    Media toEntity(MediaDTO mediaDTO);

    MediaDTO toDto(Media media);


}
