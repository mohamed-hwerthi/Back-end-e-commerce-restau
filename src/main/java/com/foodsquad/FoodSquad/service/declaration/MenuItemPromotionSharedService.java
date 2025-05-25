package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;

import java.time.LocalDate;
import java.util.List;

public interface MenuItemPromotionSharedService {

    PromotionDTO createPromotionForMenuItems(List<Long> menuItemsIds, PromotionDTO promotionDTO);

    List<MenuItemDTO>findMenuItemsRelatedToPromotion(Long promotionId) ;

    boolean hasActivePromotionOverlappingPeriod(Long menuItemId , LocalDate startDate , LocalDate endDate) ;

    void deactivatePromotionForMenuItem(Long menuItemId , Long promotionId) ;

    void addPromotionToMenuItem(Long menuItemId , Long promotionId) ;

    boolean  isMenuItemHasActivePromotionInCurrentDay(Long menuItemId) ;

    PercentageDiscountPromotion getMenuItemActivePromotionInCurrentDay(Long menuItemId) ;





}
