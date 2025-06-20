package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionType;
import com.foodsquad.FoodSquad.service.declaration.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les promotions.
 * Fournit les endpoints pour créer, récupérer, mettre à jour et supprimer des promotions,
 * ainsi que pour récupérer les promotions avec pagination.
 */
@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor

@Tag(name = "13. Promotion Management", description = "Gestion des promotions")
public class PromotionController {

    private final PromotionService promotionService;

    /**
     * Crée une nouvelle promotion.
     *
     * @param promotionDTO les données de la promotion à créer
     * @return la promotion créée
     */
    @Operation(summary = "Créer une nouvelle promotion",
            description = "Crée une promotion avec les détails fournis dans le corps de la requête.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promotion créée avec succès",
                    content = @Content(schema = @Schema(implementation = PromotionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies")
    })
    @PostMapping()
    public ResponseEntity<PromotionDTO> createPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionDTO created = promotionService.createPromotion(promotionDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Récupère une promotion par son identifiant.
     *
     * @param id l'identifiant de la promotion
     * @return la promotion correspondante
     */
    @Operation(summary = "Récupérer une promotion par ID",
            description = "Retourne la promotion correspondant à l'identifiant fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promotion trouvée",
                    content = @Content(schema = @Schema(implementation = PromotionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Promotion non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(
            @Parameter(description = "ID de la promotion à récupérer", example = "1")
            @PathVariable Long id) {
        PromotionDTO promotionDTO = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotionDTO);
    }

    /**
     * Récupère toutes les promotions avec pagination.
     *
     * @param page numéro de la page (commence à 0)
     * @param limit nombre d'éléments par page
     * @return une page de promotions
     */
    @Operation(summary = "Récupérer toutes les promotions avec pagination",
            description = "Retourne une liste paginée de promotions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste paginée des promotions",
                    content = @Content(schema = @Schema(implementation = PaginatedResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<PromotionDTO>> getAllPromotions(
            @Parameter(description = "Numéro de la page, à partir de 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Nombre d'éléments par page", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        Pageable pageable = PageRequest.of(page, limit);
        PaginatedResponseDTO<PromotionDTO> promotions = promotionService.getAllPromotions(pageable);
        return ResponseEntity.ok(promotions);
    }

    /**
     * Met à jour une promotion existante.
     *
     * @param id l'identifiant de la promotion à mettre à jour
     * @param promotionDTO les nouvelles données de la promotion
     * @return la promotion mise à jour
     */
    @Operation(summary = "Mettre à jour une promotion",
            description = "Met à jour les détails d'une promotion existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promotion mise à jour avec succès",
                    content = @Content(schema = @Schema(implementation = PromotionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies"),
            @ApiResponse(responseCode = "404", description = "Promotion non trouvée")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PromotionDTO> updatePromotion(
            @Parameter(description = "ID de la promotion à mettre à jour", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionDTO updated = promotionService.updatePromotion(id, promotionDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime une promotion par son identifiant.
     *
     * @param id l'identifiant de la promotion à supprimer
     * @return une réponse sans contenu
     */
    @Operation(summary = "Supprimer une promotion",
            description = "Supprime la promotion correspondant à l'identifiant fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promotion supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Promotion non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(
            @Parameter(description = "ID de la promotion à supprimer", example = "1")
            @PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }



    /**
     * Change le statut d'activation d'une promotion (active/désactive).
     *
     * @param id l'identifiant de la promotion à activer/désactiver
     * @return une réponse sans contenu
     */
    @Operation(summary = "Changer le statut d'activation d'une promotion",
            description = "Active ou désactive la promotion correspondant à l'identifiant fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Statut de la promotion modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Promotion non trouvée")
    })
    @PatchMapping("/{id}/activation")
    public ResponseEntity<Void> changePromotionActivationStatus(
            @Parameter(description = "ID de la promotion à activer/désactiver", example = "1")
            @PathVariable Long id) {
        promotionService.changePromotionActivationStatus(id);
        return ResponseEntity.noContent().build();
    }

     @GetMapping("/types")
    ResponseEntity  <PromotionType[]> getAllPromotionTypes() {
        PromotionType[] promotionTypes =  PromotionType.values();
        return ResponseEntity.ok(promotionTypes);
    }







}
