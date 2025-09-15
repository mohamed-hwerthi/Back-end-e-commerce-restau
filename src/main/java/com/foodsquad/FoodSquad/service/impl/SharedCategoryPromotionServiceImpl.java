package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.exception.CategoryActivePromotionInAPeriodException;
import com.foodsquad.FoodSquad.exception.MenuItemHasActivePromotionInAPeriodException;
import com.foodsquad.FoodSquad.mapper.CategoryMapper;
import com.foodsquad.FoodSquad.mapper.PromotionMapper;
import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionType;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import com.foodsquad.FoodSquad.service.declaration.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SharedCategoryPromotionServiceImpl implements SharedCategoryPromotionService {

    private final CategoryService categoryService;
    private final PromotionService promotionService;
    private final CategoryMapper categoryMapper;
    private final PromotionMapper promotionMapper;
    private final MenuItemService menuItemService;
    private final MenuItemPromotionSharedService menuItemPromotionSharedService;

    @Override
    public PromotionDTO createPromotionForCategories(List<UUID> categoriesIds, PromotionDTO promotionDTO) {
        List<CategoryDTO> categories = getCategories(categoriesIds);

        if (promotionDTO.getPromotionType().equals(PromotionType.DISCOUNT)) {
            return createPercentageDiscountPromotion(promotionDTO, categories);
        }
        return null;
    }

    @Override
    public void addPromotionToCategory(UUID categoryId, UUID promotionId) {
        Category category = categoryService.findCategory(categoryId);
        Promotion promotion = promotionService.getPromotion(promotionId);


        if (hasActivePromotionOverlappingPeriod(categoryId, promotion.getStartDate(), promotion.getEndDate())) {
            throw new CategoryActivePromotionInAPeriodException("Category has active promotion in this period");
        }

        category.getPromotions().add(promotion);
        categoryService.saveCategory(category);
    }

    @Override
    public void deactivatePromotionForCategory(UUID categoryId, UUID promotionId) {
        Category category = categoryService.findCategory(categoryId);

        category.getPromotions().removeIf(promotion -> promotion.getId().equals(promotionId));

        categoryService.saveCategory(category);
    }

    @Override
    public boolean hasActivePromotionOverlappingPeriod(UUID categoryId, LocalDate startDate, LocalDate endDate) {

        Category category = categoryService.findCategory(categoryId);
        List<MenuItem> menuItems = menuItemService.findByCategory(category);
        checkIfMenuItemsOfThePromotionHasActivePromotion(menuItems, startDate, endDate);
        return category.getPromotions().stream()
                .filter(Promotion::isActive)
                .anyMatch(promotion -> arePeriodsOverlapping(
                        promotion.getStartDate(),
                        promotion.getEndDate(),
                        startDate,
                        endDate));
    }

    @Override
    public List<CategoryDTO> findCategoriesRelatedToPromotion(UUID promotionId) {
        Promotion promotion = promotionService.getPromotion(promotionId);
        return categoryMapper.toDTOList(promotion.getCategories());
    }

    private void checkIfMenuItemsOfThePromotionHasActivePromotion(List<MenuItem> menuItems, LocalDate startDate, LocalDate endDate) {
        menuItems.forEach(
                menuItem -> {
                    if (menuItemPromotionSharedService.hasActivePromotionOverlappingPeriod(menuItem.getId(), startDate, endDate)) {
                        throw new MenuItemHasActivePromotionInAPeriodException("menu item has active promotion ");
                    }
                }
        );
    }

    private List<CategoryDTO> getCategories(List<UUID> categoriesIds) {
        return categoriesIds.stream()
                .map(categoryService::findCategoryById)
                .toList();
    }

    private PromotionDTO createPercentageDiscountPromotion(PromotionDTO promotionDTO, List<CategoryDTO> categories) {
        PercentageDiscountPromotion promotion = promotionMapper.mapPromotionDTOToPercentageDiscountPromotion(promotionDTO);

        Promotion savedPromotion = promotionService.savePromotion(promotion);

        associatePromotionWithCategories(savedPromotion, categories);

        return promotionMapper.toDTO(savedPromotion);
    }

    private void associatePromotionWithCategories(Promotion promotion, List<CategoryDTO> categoryDTOS) {
        List<Category> categories = categoryDTOS.stream()
                .map(categoryDTO -> {
                    Category category = categoryMapper.toEntity(categoryDTO);
                    category.getPromotions().add(promotion);
                    return category;
                })
                .toList();

        categoryService.saveCategories(categories);
    }

    /**
     * Checks if two date periods overlap (at least one day in common).
     */
    private boolean arePeriodsOverlapping(LocalDate period1Start, LocalDate period1End, LocalDate period2Start, LocalDate period2End) {
        return !period1End.isBefore(period2Start) && !period1Start.isAfter(period2End);
    }
}