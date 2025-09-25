package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.dto.SupplementOptionDTO;
import com.foodsquad.FoodSquad.service.SupplementOptionService;
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
@RequestMapping("/api/supplement-options")
@Tag(name = "Supplement Options", description = "API for managing supplement options")
public class SupplementOptionController {

    private static final Logger logger = LoggerFactory.getLogger(SupplementOptionController.class);

    @Autowired
    private SupplementOptionService supplementOptionService;

    /**
     * Get all supplement options.
     *
     * @return List of supplement options.
     */
    @GetMapping
    @Operation(summary = "Get all supplement options")
    public ResponseEntity<List<SupplementOptionDTO>> getAllSupplementOptions() {
        logger.info("Fetching all supplement options");
        return ResponseEntity.ok(supplementOptionService.getAllSupplementOptions());
    }

    /**
     * Create a new supplement option.
     *
     * @param supplementOptionDTO Supplement option data.
     * @return Created supplement option.
     */
    @PostMapping
    @Operation(summary = "Create a new supplement option")
    public ResponseEntity<SupplementOptionDTO> createSupplementOption(@RequestBody SupplementOptionDTO supplementOptionDTO) {
        logger.info("Creating a new supplement option: {}", supplementOptionDTO);
        return ResponseEntity.ok(supplementOptionService.createSupplementOption(supplementOptionDTO));
    }

    /**
     * Delete a supplement option by ID.
     *
     * @param id Supplement option ID.
     * @return Response entity.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a supplement option by ID")
    public ResponseEntity<Void> deleteSupplementOption(@PathVariable UUID id) {
        logger.info("Deleting supplement option with ID: {}", id);
        supplementOptionService.deleteSupplementOption(id);
        return ResponseEntity.noContent().build();
    }
}
