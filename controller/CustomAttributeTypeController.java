package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.entity.CustomAttributeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for retrieving available custom attribute types.
 *
 * <p>This endpoint returns the full list of supported {@link CustomAttributeType}
 * values. It can be used by client applications (e.g., admin UIs) to populate
 * dropdowns or perform validation when creating custom attributes.</p>
 */
@RestController
@RequestMapping("/api/custom-attribute-types")
@Tag(name = "Custom Attribute Types", description = "API for listing supported custom attribute types")
public class CustomAttributeTypeController {

    private static final Logger logger = LoggerFactory.getLogger(CustomAttributeTypeController.class);

    /**
     * Get all available custom attribute types.
     *
     * @return HTTP 200 containing the list of enum names representing the supported custom attribute types.
     */
    @GetMapping
    @Operation(summary = "Get all custom attribute types")
    public ResponseEntity<List<String>> getAllCustomAttributeTypes() {
        logger.info("Fetching all custom attribute types");
        List<String> types = Arrays.stream(CustomAttributeType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        logger.debug("Found {} custom attribute types: {}", types.size(), types);
        return ResponseEntity.ok(types);
    }
}
