package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.service.admin.dec.SharedCategoryPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category-promotions")
@RequiredArgsConstructor
public class SharedCategoryPromotionController {

    private final SharedCategoryPromotionService categoryPromotionService;

    /**
     * Create a promotion for a list of category IDs.
     */
    @PostMapping("/create")
    public ResponseEntity<PromotionDTO> createPromotionForCategories(
            @RequestParam List<UUID> categoriesIds,
            @RequestBody PromotionDTO promotionDTO) {

        PromotionDTO createdPromotion = categoryPromotionService.createPromotionForCategories(categoriesIds, promotionDTO);
        if (createdPromotion == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(createdPromotion);
    }

    /**
     * Add an existing promotion to a category.
     */
    @PostMapping("category/{categoryId}/promotions/{promotionId}")
    public ResponseEntity<Void> addPromotionToCategory(
            @PathVariable UUID categoryId,
            @PathVariable UUID promotionId) {

        categoryPromotionService.addPromotionToCategory(categoryId, promotionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Deactivate a promotion for a category.
     */
    @DeleteMapping("/{categoryId}/promotions/{promotionId}/deactivate")
    public ResponseEntity<Void> deactivatePromotionForCategory(
            @PathVariable UUID categoryId,
            @PathVariable UUID promotionId) {

        categoryPromotionService.deactivatePromotionForCategory(categoryId, promotionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if there is an active promotion overlapping a given period for a category.
     */
    @GetMapping("/{categoryId}/active-promotion-overlap")
    public ResponseEntity<Boolean> hasActivePromotionOverlappingPeriod(
            @PathVariable UUID categoryId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        boolean hasOverlap = categoryPromotionService.hasActivePromotionOverlappingPeriod(categoryId, startDate, endDate);
        return ResponseEntity.ok(hasOverlap);
    }

    /**
     * Get the list of categories related to a promotion.
     */
    @GetMapping("/{promotionId}/categories")
    public ResponseEntity<List<CategoryDTO>> findCategoriesRelatedToPromotion(@PathVariable UUID promotionId) {
        List<CategoryDTO> categories = categoryPromotionService.findCategoriesRelatedToPromotion(promotionId);
        return ResponseEntity.ok(categories);
    }
}
