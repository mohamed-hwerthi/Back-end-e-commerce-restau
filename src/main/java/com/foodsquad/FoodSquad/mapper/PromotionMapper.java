package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    PromotionDTO toDTO(Promotion promotion);

    Promotion toEntity(PromotionDTO promotionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PromotionDTO dto, @MappingTarget Promotion entity);


    PercentageDiscountPromotion mapPromotionDTOToPercentageDiscountPromotion(PromotionDTO promotionDTO);

    PromotionDTO mapPromotionPercentageDiscountPromotionToPromotionDTO(PercentageDiscountPromotion promotion);
}

