package com.foodsquad.FoodSquad.service.declaration;


import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PromotionService {

    PromotionDTO createPromotion(PromotionDTO promotionDTO);

    PromotionDTO getPromotionById(Long id);

    PaginatedResponseDTO<PromotionDTO> getAllPromotions(Pageable pageable);

    PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO);

    void  deletePromotion(Long id);

    Promotion getPromotion(Long id );

    Promotion savePromotion(Promotion promotion);

    void   changePromotionActivationStatus(Long promotionId);


    /**
     * Returns all promotions associated with a given menu item.
     * <p>
     * This includes promotions directly linked to the menu item as well as promotions
     * linked to the categories related to the menu item.
     *
     * @param menuItemId the ID of the menu item for which to retrieve promotions
     * @return a {@code PromotionDTO} containing all relevant promotions
     */
     List<PromotionDTO>findAllPromotionForMenuItem(Long menuItemId);

    List<Promotion>findPromotionsForMenuItem(Long menuItemId);







}

