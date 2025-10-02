package com.foodsquad.FoodSquad.service.admin.impl;


import com.foodsquad.FoodSquad.mapper.PromotionMapper;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.PromotionRepository;
import com.foodsquad.FoodSquad.service.admin.dec.CategoryService;
import com.foodsquad.FoodSquad.service.admin.dec.ProductService;
import com.foodsquad.FoodSquad.service.admin.dec.PromotionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    private final ProductService ProductService;

    private final CategoryService categoryService;


    @Override
    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {

        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        Promotion saved = promotionRepository.save(promotion);
        return promotionMapper.toDTO(saved);
    }

    @Override
    public PromotionDTO getPromotionById(UUID id) {

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
    public PromotionDTO updatePromotion(UUID id, PromotionDTO promotionDTO) {

        Promotion existingPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));
        if (existingPromotion instanceof PercentageDiscountPromotion percentageDiscountPromotion) {
            promotionMapper.updatePercentageDiscountPromotionWithNewPromotion(promotionDTO, percentageDiscountPromotion);
            return promotionMapper.toDTO(promotionRepository.save(percentageDiscountPromotion));
        }

        return null;

    }

    @Override
    @Transactional
    public void deletePromotion(UUID id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));
        if (promotion.getPromotionTarget().equals(PromotionTarget.ProductS)) {
            List<Product> ProductsWithPromotion = ProductService.findByPromotion(promotion);
            ProductsWithPromotion.forEach(product -> product.getPromotions().remove(promotion));

        }
        if (promotion.getPromotionTarget().equals(PromotionTarget.CATEGORIES)) {
            List<Category> categories = categoryService.findCategoriesWithPromotions(promotion);
            categories.forEach(category -> category.getPromotions().remove(promotion)
            );

        }
        promotionRepository.delete(promotion);
    }

    @Override
    public Promotion getPromotion(UUID id) {

        return promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + id));
    }

    @Override
    public Promotion savePromotion(Promotion promotion) {

        return promotionRepository.save(promotion);
    }

    @Override
    public void changePromotionActivationStatus(UUID promotionId) {

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + promotionId));

        promotion.setActive(!promotion.isActive());
        promotionRepository.save(promotion);
    }

    @Override
    public List<PromotionDTO> findAllPromotionForProduct(UUID ProductId) {
        return promotionRepository.findAllPromotionsForProduct(ProductId).stream().map(promotionMapper::toDTO).toList();
    }

    @Override
    public List<Promotion> findPromotionsForProduct(UUID ProductId) {
        return promotionRepository.findAllPromotionsForProduct(ProductId);
    }
}

