package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.ReviewDTO;
import com.foodsquad.FoodSquad.model.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends GenericMapper<Review, ReviewDTO> {
    @Override
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ReviewDTO dto, @MappingTarget Review entity);
}