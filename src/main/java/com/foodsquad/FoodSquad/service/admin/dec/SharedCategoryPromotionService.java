package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;

import java.util.List;
import java.util.UUID;

public interface SharedCategoryPromotionService {

    PromotionDTO createPromotionForCategories(List<UUID> categoriesIds, PromotionDTO promotionDTO);

    void addPromotionToCategory(UUID categoryId, UUID promotionId);

    void deactivatePromotionForCategory(UUID categoryId, UUID promotionId);

    boolean hasActivePromotionOverlappingPeriod(UUID categoryId, java.time.LocalDate startDate, java.time.LocalDate endDate);

    List<CategoryDTO> findCategoriesRelatedToPromotion(UUID promotionId);
}
