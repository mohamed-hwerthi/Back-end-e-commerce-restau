package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.VariantAttributeDTO;
import com.foodsquad.FoodSquad.service.declaration.VariantAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/variant-attributes")
@RequiredArgsConstructor
@Tag(name = "Variant Attributes", description = "APIs for managing variant attributes")
public class VariantAttributeController {

    private final VariantAttributeService variantAttributeService;



    @GetMapping("/{id}")
    @Operation(summary = "Get a variant attribute by ID")
    public ResponseEntity<VariantAttributeDTO> getVariantAttributeById(@PathVariable UUID id) {
        log.info("Fetching variant attribute with ID: {}", id);
        VariantAttributeDTO variantAttribute = variantAttributeService.getVariantAttributeById(id);
        return ResponseEntity.ok(variantAttribute);
    }

    @GetMapping("/variant/{variantId}")
    @Operation(summary = "Get all attributes for a specific variant")
    public ResponseEntity<List<VariantAttributeDTO>> getVariantAttributesByVariantId(
            @PathVariable UUID variantId) {
        log.info("Fetching all attributes for variant ID: {}", variantId);
        List<VariantAttributeDTO> variantAttributes = variantAttributeService.getVariantAttributesByVariantId(variantId);
        return ResponseEntity.ok(variantAttributes);
    }



    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a variant attribute")
    public ResponseEntity<Void> deleteVariantAttribute(@PathVariable UUID id) {
        log.info("Deleting variant attribute with ID: {}", id);
        variantAttributeService.deleteVariantAttribute(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/variant/{variantId}")
    @Operation(summary = "Delete all attributes for a variant")
    public ResponseEntity<Void> deleteAllVariantAttributes(@PathVariable UUID variantId) {
        log.info("Deleting all attributes for variant ID: {}", variantId);
        variantAttributeService.deleteAllByVariantId(variantId);
        return ResponseEntity.noContent().build();
    }
}
