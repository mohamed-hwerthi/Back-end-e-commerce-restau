package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.dto.ProductOptionDTO;
import com.foodsquad.FoodSquad.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-options")
@Tag(name = "Supplement Options", description = "API for managing supplement options")
public class ProductOptionController {

    private static final Logger logger = LoggerFactory.getLogger(ProductOptionController.class);

    @Autowired
    private ProductOptionService supplementOptionService;

    /**
     * Get all supplement options.
     *
     * @return List of supplement options.
     */
    @GetMapping
    @Operation(summary = "Get all supplement options")
    public ResponseEntity<List<ProductOptionDTO>> getAllProductOptions() {
        logger.info("Fetching all supplement options");
        return ResponseEntity.ok(supplementOptionService.getAllProductOptions());
    }

    /**
     * Create a new supplement option.
     *
     * @param supplementOptionDTO Supplement option data.
     * @return Created supplement option.
     */
    @PostMapping
    @Operation(summary = "Create a new supplement option")
    public ResponseEntity<ProductOptionDTO> createProductOption(@RequestBody ProductOptionDTO supplementOptionDTO) {
        logger.info("Creating a new supplement option: {}", supplementOptionDTO);
        return ResponseEntity.ok(supplementOptionService.createProductOption(supplementOptionDTO));
    }

    /**
     * Delete a supplement option by ID.
     *
     * @param id Supplement option ID.
     * @return Response entity.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a supplement option by ID")
    public ResponseEntity<Void> deleteProductOption(@PathVariable UUID id) {
        logger.info("Deleting supplement option with ID: {}", id);
        supplementOptionService.deleteProductOption(id);
        return ResponseEntity.noContent().build();
    }
}
