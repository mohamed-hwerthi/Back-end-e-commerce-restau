package com.foodsquad.FoodSquad.service.helpers;


import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.service.declaration.MenuItemPromotionSharedService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class MenuItemDiscountPriceCalculator {

    private final MenuItemPromotionSharedService promotionService;

    public MenuItemDiscountPriceCalculator( @Lazy MenuItemPromotionSharedService promotionService) {

        this.promotionService = promotionService;
    }

    public double calculateDiscountedPrice(MenuItem menuItem) {
        Long menuItemId = menuItem.getId();
        if (!promotionService.isMenuItemHasActivePromotionInCurrentDay(menuItemId)) {
            return menuItem.getPrice();
        }

        PercentageDiscountPromotion promotion = promotionService.getMenuItemActivePromotionInCurrentDay(menuItemId);
        if (promotion == null || promotion.getDiscountPercentage() == null || promotion.getDiscountPercentage() <= 0) {
            return menuItem.getPrice();
        }

        return applyDiscount(menuItem.getPrice(), promotion.getDiscountPercentage());
    }

    private double applyDiscount(double originalPrice, double discountPercentage) {
        return originalPrice * (1 - discountPercentage / 100.0);
    }
}