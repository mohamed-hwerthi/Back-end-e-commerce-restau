package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CustomAttributeDTO;
import com.foodsquad.FoodSquad.service.declaration.CustomAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing Custom Attributes.
 *
 * <p>Provides CRUD operations similar to {@link TimbreController} style,
 * including SLF4J logging and Swagger/OpenAPI documentation.</p>
 */
@RestController
@RequestMapping("/api/custom-attributes")
@RequiredArgsConstructor
@Tag(name = "Custom Attribute Management", description = "APIs for managing custom attributes")
public class CustomAttributeController {

    private static final Logger logger = LoggerFactory.getLogger(CustomAttributeController.class);

    private final CustomAttributeService customAttributeService;


    @Operation(summary = "Retrieve all custom attributes")
    @GetMapping
    public ResponseEntity<List<CustomAttributeDTO>> getAll() {
        logger.info("Received request to get all CustomAttributes");
        List<CustomAttributeDTO> list = customAttributeService.findAll();
        logger.info("Returning {} CustomAttributes", list.size());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Retrieve a custom attribute by ID")
    @ApiResponse(responseCode = "200", description = "Custom attribute found")
    @ApiResponse(responseCode = "404", description = "Custom attribute not found")
    @GetMapping("/{id}")
    public ResponseEntity<CustomAttributeDTO> getById(@PathVariable UUID id) {
        logger.info("Received request to get CustomAttribute by id: {}", id);
        CustomAttributeDTO dto = customAttributeService.findById(id);
        if (dto == null) {
            logger.warn("CustomAttribute not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Found CustomAttribute: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete a custom attribute by ID")
    @ApiResponse(responseCode = "204", description = "Custom attribute deleted successfully")
    @ApiResponse(responseCode = "404", description = "Custom attribute not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        logger.info("Received request to delete CustomAttribute id: {}", id);
        CustomAttributeDTO dto = customAttributeService.findById(id);
        if (dto == null) {
            logger.warn("CustomAttribute not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        customAttributeService.delete(id);
        logger.info("Deleted CustomAttribute with id: {}", id);
        return ResponseEntity.noContent().build();
    }


}
