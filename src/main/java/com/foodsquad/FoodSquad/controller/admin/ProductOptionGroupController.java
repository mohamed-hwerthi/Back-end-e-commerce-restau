package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;
import com.foodsquad.FoodSquad.service.admin.dec.ProductOptionGroupService;
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
@RequestMapping("/api/supplement-groups")
@Tag(name = "Supplement Groups", description = "API for managing supplement groups")
public class ProductOptionGroupController {

    private static final Logger logger = LoggerFactory.getLogger(ProductOptionGroupController.class);

    @Autowired
    private ProductOptionGroupService supplementGroupService;

    /**
     * Get all supplement groups.
     *
     * @return List of supplement groups.
     */
    @GetMapping
    @Operation(summary = "Get all supplement groups")
    public ResponseEntity<List<ProductOptionGroupDTO>> getAllProductOptionGroups() {
        logger.info("Fetching all supplement groups");
        return ResponseEntity.ok(supplementGroupService.getAllProductOptionGroups());
    }

    /**
     * Create a new supplement group.
     *
     * @param supplementGroupDTO Supplement group data.
     * @return Created supplement group.
     */
    @PostMapping
    @Operation(summary = "Create a new supplement group")
    public ResponseEntity<ProductOptionGroupDTO> createProductOptionGroup(@RequestBody ProductOptionGroupDTO supplementGroupDTO) {
        logger.info("Creating a new supplement group: {}", supplementGroupDTO);
        return ResponseEntity.ok(supplementGroupService.createProductOptionGroup(supplementGroupDTO));
    }

    /**
     * Delete a supplement group by ID.
     *
     * @param id Supplement group ID.
     * @return Response entity.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a supplement group by ID")
    public ResponseEntity<Void> deleteProductOptionGroup(@PathVariable UUID id) {
        logger.info("Deleting supplement group with ID: {}", id);
        supplementGroupService.deleteProductOptionGroup(id);
        return ResponseEntity.noContent().build();
    }
}
