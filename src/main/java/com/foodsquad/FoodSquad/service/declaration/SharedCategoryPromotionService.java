package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;

import java.util.List;
import java.util.UUID;

public interface SharedCategoryPromotionService {

    PromotionDTO createPromotionForCategories(List<UUID> categoriesIds, PromotionDTO promotionDTO);

    void addPromotionToCategory(UUID categoryId, Long promotionId);

    void deactivatePromotionForCategory(UUID categoryId, Long promotionId);

    boolean hasActivePromotionOverlappingPeriod(UUID categoryId, java.time.LocalDate startDate, java.time.LocalDate endDate);

    List<CategoryDTO> findCategoriesRelatedToPromotion(Long promotionId);
}
