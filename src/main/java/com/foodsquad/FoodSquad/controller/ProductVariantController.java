package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.ProductVariantDTO;
import com.foodsquad.FoodSquad.service.declaration.ProductVariantService;
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
@RequestMapping("/api/variants")
@RequiredArgsConstructor
@Tag(name = "Product Variants", description = "APIs for managing product variants")
public class ProductVariantController {

    private final ProductVariantService variantService;

    @PostMapping
    @Operation(summary = "Create a new product variant")
    public ResponseEntity<ProductVariantDTO> createVariant(@Valid @RequestBody ProductVariantDTO variantDTO) {
        log.info("Received request to create a new product variant");
        ProductVariantDTO createdVariant = variantService.createVariant(variantDTO);
        return new ResponseEntity<>(createdVariant, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product variant by ID")
    public ResponseEntity<ProductVariantDTO> getVariantById(@PathVariable UUID id) {
        log.info("Fetching product variant with ID: {}", id);
        ProductVariantDTO variant = variantService.getVariantById(id);
        return ResponseEntity.ok(variant);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all variants for a product")
    public ResponseEntity<List<ProductVariantDTO>> getVariantsByProductId(@PathVariable UUID productId) {
        log.info("Fetching all variants for product with ID: {}", productId);
        List<ProductVariantDTO> variants = variantService.getVariantsByProductId(productId);
        return ResponseEntity.ok(variants);
    }

    @GetMapping
    @Operation(summary = "Get all product variants")
    public ResponseEntity<List<ProductVariantDTO>> getAllVariants() {
        log.info("Fetching all product variants");
        List<ProductVariantDTO> variants = variantService.getAllVariants();
        return ResponseEntity.ok(variants);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product variant")
    public ResponseEntity<ProductVariantDTO> updateVariant(
            @PathVariable UUID id,
            @Valid @RequestBody ProductVariantDTO variantDTO) {
        log.info("Updating product variant with ID: {}", id);
        ProductVariantDTO updatedVariant = variantService.updateVariant(id, variantDTO);
        return ResponseEntity.ok(updatedVariant);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product variant")
    public ResponseEntity<Void> deleteVariant(@PathVariable UUID id) {
        log.info("Deleting product variant with ID: {}", id);
        variantService.deleteVariant(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update stock quantity for a product variant")
    public ResponseEntity<ProductVariantDTO> updateStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        log.info("Updating stock for product variant with ID: {}", id);
        ProductVariantDTO updatedVariant = variantService.updateStock(id, quantity);
        return ResponseEntity.ok(updatedVariant);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Toggle variant status (active/inactive)")
    public ResponseEntity<ProductVariantDTO> toggleVariantStatus(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "true") boolean active) {
        log.info("Toggling status for product variant with ID: {} to {}", id, active ? "active" : "inactive");
        ProductVariantDTO updatedVariant = variantService.toggleVariantStatus(id, active);
        return ResponseEntity.ok(updatedVariant);
    }
}
