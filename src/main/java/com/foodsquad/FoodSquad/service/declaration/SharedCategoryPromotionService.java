package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;

import java.util.List;

public interface SharedCategoryPromotionService {

    PromotionDTO createPromotionForCategories(List<Long> categoriesIds, PromotionDTO promotionDTO);

    void addPromotionToCategory(Long categoryId, Long promotionId);

    void deactivatePromotionForCategory(Long categoryId, Long promotionId);

    boolean hasActivePromotionOverlappingPeriod(Long categoryId, java.time.LocalDate startDate, java.time.LocalDate endDate);

    List<CategoryDTO> findCategoriesRelatedToPromotion(Long promotionId);
}
