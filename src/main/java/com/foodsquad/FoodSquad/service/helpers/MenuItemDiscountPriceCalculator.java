package com.foodsquad.FoodSquad.service.helpers;


import com.foodsquad.FoodSquad.model.dto.DiscountType;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.service.declaration.MenuItemPromotionSharedService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
public class MenuItemDiscountPriceCalculator {

    private final MenuItemPromotionSharedService promotionService;

    public MenuItemDiscountPriceCalculator(@Lazy MenuItemPromotionSharedService promotionService) {

        this.promotionService = promotionService;
    }

    public BigDecimal calculateDiscountedPrice(Product product) {
        UUID menuItemId = product.getId();
        if (!promotionService.isMenuItemHasActivePromotionInCurrentDay(menuItemId)) {
            return product.getPrice();
        }

        PercentageDiscountPromotion promotion = promotionService.getMenuItemActivePromotionInCurrentDay(menuItemId);


        if (promotion.getDiscountType().equals(DiscountType.BY_PERCENTAGE)) {
            return applyDiscount(product.getPrice(), promotion.getDiscountPercentage());

        }
        if (promotion.getDiscountType().equals(DiscountType.BY_AMOUNT)) {
            return promotion.getPromotionalPrice();

        }
        return product.getPrice();
    }

    private BigDecimal applyDiscount(BigDecimal originalPrice, Integer discountPercentage) {
        BigDecimal discount = BigDecimal.valueOf(discountPercentage);
        BigDecimal discountFactor = BigDecimal.ONE.subtract(
                discount.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        );
        return originalPrice.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    }


}