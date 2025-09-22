package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProductPromotionSharedService {

    PromotionDTO createPromotionForProducts(List<UUID> ProductsIds, PromotionDTO promotionDTO);

    List<ProductDTO> findProductsRelatedToPromotion(UUID promotionId);

    boolean hasActivePromotionOverlappingPeriod(UUID ProductId, LocalDate startDate, LocalDate endDate);

    void deactivatePromotionForProduct(UUID ProductId, UUID promotionId);

    void addPromotionToProduct(UUID ProductId, UUID promotionId);

    boolean isProductHasActivePromotionInCurrentDay(UUID ProductId);

    PercentageDiscountPromotion getProductActivePromotionInCurrentDay(UUID ProductId);


}
