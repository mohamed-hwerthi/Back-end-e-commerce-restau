

package com.foodsquad.FoodSquad.service.impl;


import com.foodsquad.FoodSquad.mapper.MenuItemMapper;
import com.foodsquad.FoodSquad.mapper.PromotionMapper;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import com.foodsquad.FoodSquad.repository.PromotionRepository;
import com.foodsquad.FoodSquad.service.declaration.PromotionService;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    private final MenuItemService menuItemService;

    private final MenuItemMapper menuItemMapper ;



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

        List<PromotionDTO> promotions  = promotionRepository.findAll(pageable).stream().map(promotionMapper::toDTO).toList();
        return new PaginatedResponseDTO<>(promotions, promotionRepository.count());

    }

    @Override
    public PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO) {
        Promotion existingPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));

        promotionMapper.updateEntityFromDTO(promotionDTO, existingPromotion);
        Promotion updated = promotionRepository.save(existingPromotion);
        return promotionMapper.toDTO(updated);
    }

    @Override
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new EntityNotFoundException("Promotion not found with id: " + id);
        }
        promotionRepository.deleteById(id);
    }

    @Override
    public Promotion getPromotion(Long id) {

        return  promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));
    }

    @Override
    public Promotion savePromotion(Promotion promotion) {

        return  promotionRepository.save(promotion);
    }

    @Override
    public void  changePromotionActivationStatus(Long promotionId) {

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + promotionId));

        promotion.setActive(!promotion.isActive());
    }

}

