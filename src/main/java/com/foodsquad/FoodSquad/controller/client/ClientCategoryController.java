package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.client.ClientCategoryDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Client-facing Category endpoints.
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
@RestController
@RequestMapping("/api/client/categories")
@RequiredArgsConstructor
@Slf4j
public class ClientCategoryController {

    private final ClientCategoryService clientCategoryService;

    @Operation(summary = "List all categories for storefront")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ClientCategoryDTO>> findAll() {
        log.info("Client request: list categories");
        return ResponseEntity.ok(clientCategoryService.findAll());
    }

    @Operation(summary = "Get a category by ID for storefront")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientCategoryDTO> findById(@PathVariable UUID id) {
        log.info("Client request: get category id={}", id);
        return ResponseEntity.ok(clientCategoryService.findById(id));
    }
}
