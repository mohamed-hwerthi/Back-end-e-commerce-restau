

package com.foodsquad.FoodSquad.service.impl;


import com.foodsquad.FoodSquad.mapper.PromotionMapper;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import com.foodsquad.FoodSquad.repository.PromotionRepository;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import com.foodsquad.FoodSquad.service.declaration.PromotionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    private final MenuItemService menuItemService ;


    @Override
    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {

        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        Promotion saved = promotionRepository.save(promotion);
        return promotionMapper.toDTO(saved);
    }

    @Override
    public PromotionDTO getPromotionById(Long id) {

        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));
        return promotionMapper.toDTO(promotion);
    }

    @Override
    public PaginatedResponseDTO<PromotionDTO> getAllPromotions(Pageable pageable) {

        Page<Promotion> promotions = promotionRepository.findAll(pageable);
        List<PromotionDTO> promotionDTOS = promotions.stream().map(promotion -> {
            if (promotion instanceof PercentageDiscountPromotion) {
                return promotionMapper.mapPromotionPercentageDiscountPromotionToPromotionDTO((PercentageDiscountPromotion) promotion);
            }
            return promotionMapper.toDTO(promotion);
        }).toList();
        return new PaginatedResponseDTO<>(promotionDTOS, promotionRepository.count());

    }

    @Override
    public PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO) {

        Promotion existingPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));
        if(existingPromotion instanceof  PercentageDiscountPromotion percentageDiscountPromotion ){
            promotionMapper.updatePercentageDiscountPromotionWithNewPromotion(promotionDTO , percentageDiscountPromotion);
            return promotionMapper.toDTO(promotionRepository.save(percentageDiscountPromotion));
        }

        return null  ;

    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));

        List<MenuItem> menuItemsWithPromotion = menuItemService.findByPromotion(promotion);
        menuItemsWithPromotion.forEach(menuItem -> menuItem.getPromotions().remove(promotion));
        promotionRepository.delete(promotion);
    }

    @Override
    public Promotion getPromotion(Long id) {

        return promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));
    }

    @Override
    public Promotion savePromotion(Promotion promotion) {

        return promotionRepository.save(promotion);
    }

    @Override
    public void changePromotionActivationStatus(Long promotionId) {

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + promotionId));

        promotion.setActive(!promotion.isActive());
        promotionRepository.save(promotion);
    }

}

