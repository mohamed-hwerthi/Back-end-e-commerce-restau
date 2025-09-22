package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionWithProductsRequestDTO;
import com.foodsquad.FoodSquad.service.declaration.ProductPromotionSharedService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items-promotions")
@RequiredArgsConstructor
public class SharedProductPromotionController {

    private final ProductPromotionSharedService promotionSharedService;

    /**
     * Crée une promotion pour une liste d'items de menu.
     */
    @PostMapping("")
    public ResponseEntity<PromotionDTO> createPromotionForProducts(
            @RequestBody PromotionWithProductsRequestDTO promotionWithProductsRequestDTO
    ) {
        PromotionDTO createdPromotion = promotionSharedService.createPromotionForProducts(promotionWithProductsRequestDTO.getProductsIds(), promotionWithProductsRequestDTO.getPromotion());
        if (createdPromotion == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(createdPromotion);
    }

    /**
     * Récupère la liste des items de menu liés à une promotion donnée.
     */
    @GetMapping("/{promotionId}/products")
    public ResponseEntity<List<ProductDTO>> findProductsRelatedToPromotion(@PathVariable UUID promotionId) {
        List<ProductDTO> products = promotionSharedService.findProductsRelatedToPromotion(promotionId);
        return ResponseEntity.ok(products);
    }

    /**
     * Vérifie s'il existe une promotion active qui chevauche une période donnée pour un item de menu.
     */
    @GetMapping("/{ProductId}/active-promotion-overlap")
    public ResponseEntity<Boolean> hasActivePromotionOverlappingPeriod(
            @PathVariable UUID ProductId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        boolean hasOverlap = promotionSharedService.hasActivePromotionOverlappingPeriod(ProductId, startDate, endDate);
        return ResponseEntity.ok(hasOverlap);
    }

    /**
     * Désactive une promotion pour un item de menu donné.
     */
    @DeleteMapping("/{ProductId}/promotions/{promotionId}/deactivate")
    public ResponseEntity<Void> desactivatePromotionForProduct(
            @PathVariable UUID ProductId,
            @PathVariable UUID promotionId) {

        promotionSharedService.deactivatePromotionForProduct(ProductId, promotionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ProductId}/promotions/{promotionId}/add")
    public ResponseEntity<Void> addPromotionToProduct(
            @PathVariable UUID ProductId,
            @PathVariable UUID promotionId) {

        promotionSharedService.addPromotionToProduct(ProductId, promotionId);
        return ResponseEntity.ok().build();
    }


}
