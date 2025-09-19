package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.AttributeValueDTO;
import com.foodsquad.FoodSquad.service.declaration.AttributeValueService;
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
@RequestMapping("/api/attribute-values")
@RequiredArgsConstructor
@Tag(name = "Attribute Values", description = "APIs for managing attribute values")
public class AttributeValueController {

    private final AttributeValueService attributeValueService;

    @PostMapping
    @Operation(summary = "Create a new attribute value")
    public ResponseEntity<AttributeValueDTO> createAttributeValue(
            @Valid @RequestBody AttributeValueDTO attributeValueDTO) {
        log.info("Received request to create a new attribute value");
        AttributeValueDTO createdValue = attributeValueService.createAttributeValue(attributeValueDTO);
        return new ResponseEntity<>(createdValue, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an attribute value by ID")
    public ResponseEntity<AttributeValueDTO> getAttributeValueById(@PathVariable UUID id) {
        log.info("Fetching attribute value with ID: {}", id);
        AttributeValueDTO attributeValue = attributeValueService.getAttributeValueById(id);
        return ResponseEntity.ok(attributeValue);
    }

    @GetMapping("/type/{attributeTypeId}")
    @Operation(summary = "Get all attribute values for a specific type")
    public ResponseEntity<List<AttributeValueDTO>> getAttributeValuesByType(
            @PathVariable UUID attributeTypeId) {
        log.info("Fetching all attribute values for type ID: {}", attributeTypeId);
        List<AttributeValueDTO> values = attributeValueService.getAttributeValuesByType(attributeTypeId);
        return ResponseEntity.ok(values);
    }

    @GetMapping
    @Operation(summary = "Get all attribute values")
    public ResponseEntity<List<AttributeValueDTO>> getAllAttributeValues() {
        log.info("Fetching all attribute values");
        List<AttributeValueDTO> values = attributeValueService.getAllAttributeValues();
        return ResponseEntity.ok(values);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an attribute value")
    public ResponseEntity<AttributeValueDTO> updateAttributeValue(
            @PathVariable UUID id,
            @Valid @RequestBody AttributeValueDTO attributeValueDTO) {
        log.info("Updating attribute value with ID: {}", id);
        AttributeValueDTO updatedValue = attributeValueService.updateAttributeValue(id, attributeValueDTO);
        return ResponseEntity.ok(updatedValue);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an attribute value")
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable UUID id) {
        log.info("Deleting attribute value with ID: {}", id);
        attributeValueService.deleteAttributeValue(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/by-types")
    @Operation(summary = "Get attribute values by multiple type IDs")
    public ResponseEntity<List<AttributeValueDTO>> getAttributeValuesByTypes(
            @RequestBody List<UUID> attributeTypeIds) {
        log.info("Fetching attribute values for types: {}", attributeTypeIds);
        List<AttributeValueDTO> values = attributeValueService.getAttributeValuesByTypeIn(attributeTypeIds);
        return ResponseEntity.ok(values);
    }
}
