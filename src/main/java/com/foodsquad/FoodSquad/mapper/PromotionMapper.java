package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class})
public interface PromotionMapper {

    PromotionDTO toDTO(Promotion promotion);

    Promotion toEntity(PromotionDTO promotionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PromotionDTO dto, @MappingTarget Promotion entity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "active" , ignore = true)
    void updatePercentageDiscountPromotionWithNewPromotion(PromotionDTO dto, @MappingTarget PercentageDiscountPromotion entity);


    PercentageDiscountPromotion mapPromotionDTOToPercentageDiscountPromotion(PromotionDTO promotionDTO);

    @Mapping(target = "promotionType", constant = "DISCOUNT")
    @Mapping(target = "menuItems" , source = "menuItems")
    @Mapping(target = "categories" , source = "categories   ")

    PromotionDTO mapPromotionPercentageDiscountPromotionToPromotionDTO(PercentageDiscountPromotion promotion);


}

