package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MenuItemPromotionSharedService {

    PromotionDTO createPromotionForMenuItems(List<UUID> menuItemsIds, PromotionDTO promotionDTO);

    List<ProductDTO> findMenuItemsRelatedToPromotion(UUID promotionId);

    boolean hasActivePromotionOverlappingPeriod(UUID menuItemId, LocalDate startDate, LocalDate endDate);

    void deactivatePromotionForMenuItem(UUID menuItemId, UUID promotionId);

    void addPromotionToMenuItem(UUID menuItemId, UUID promotionId);

    boolean isMenuItemHasActivePromotionInCurrentDay(UUID menuItemId);

    PercentageDiscountPromotion getMenuItemActivePromotionInCurrentDay(UUID menuItemId);


}
