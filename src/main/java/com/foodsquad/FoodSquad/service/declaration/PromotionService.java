package com.foodsquad.FoodSquad.service.declaration;


import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Pageable;


public interface PromotionService {

    PromotionDTO createPromotion(PromotionDTO promotionDTO);

    PromotionDTO getPromotionById(Long id);

    PaginatedResponseDTO<PromotionDTO> getAllPromotions(Pageable pageable);

    PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO);

    void  deletePromotion(Long id);

    Promotion getPromotion(Long id );

    Promotion savePromotion(Promotion promotion);

    void   changePromotionActivationStatus(Long promotionId);





}

