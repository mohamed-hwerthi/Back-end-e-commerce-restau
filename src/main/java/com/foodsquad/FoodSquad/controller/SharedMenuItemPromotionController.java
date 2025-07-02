package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionWithMenuItemsRequestDTO;
import com.foodsquad.FoodSquad.service.declaration.MenuItemPromotionSharedService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/items-promotions")
@RequiredArgsConstructor
public class SharedMenuItemPromotionController {

    private final MenuItemPromotionSharedService promotionSharedService;

    /**
     * Crée une promotion pour une liste d'items de menu.
     */
    @PostMapping("")
    public ResponseEntity<PromotionDTO> createPromotionForMenuItems(
            @RequestBody PromotionWithMenuItemsRequestDTO promotionWithMenuItemsRequestDTO
            ) {
        PromotionDTO createdPromotion = promotionSharedService.createPromotionForMenuItems(promotionWithMenuItemsRequestDTO.getMenuItemsIds(), promotionWithMenuItemsRequestDTO.getPromotion());
        if (createdPromotion == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(createdPromotion);
    }

    /**
     * Récupère la liste des items de menu liés à une promotion donnée.
     */
    @GetMapping("/{promotionId}/menu-items")
    public ResponseEntity<List<MenuItemDTO>> findMenuItemsRelatedToPromotion(@PathVariable Long promotionId) {
        List<MenuItemDTO> menuItems = promotionSharedService.findMenuItemsRelatedToPromotion(promotionId);
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Vérifie s'il existe une promotion active qui chevauche une période donnée pour un item de menu.
     */
    @GetMapping("/{menuItemId}/active-promotion-overlap")
    public ResponseEntity<Boolean> hasActivePromotionOverlappingPeriod(
            @PathVariable Long menuItemId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        boolean hasOverlap = promotionSharedService.hasActivePromotionOverlappingPeriod(menuItemId, startDate, endDate);
        return ResponseEntity.ok(hasOverlap);
    }

    /**
     * Désactive une promotion pour un item de menu donné.
     */
    @DeleteMapping("/{menuItemId}/promotions/{promotionId}/deactivate")
    public ResponseEntity<Void> desactivatePromotionForMenuItem(
            @PathVariable Long menuItemId,
            @PathVariable Long promotionId) {

        promotionSharedService.deactivatePromotionForMenuItem(menuItemId, promotionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{menuItemId}/promotions/{promotionId}/add")
    public ResponseEntity<Void> addPromotionToMenuItem(
            @PathVariable Long menuItemId,
            @PathVariable Long promotionId) {

        promotionSharedService.addPromotionToMenuItem(menuItemId, promotionId);
        return ResponseEntity.ok().build();
    }



}
