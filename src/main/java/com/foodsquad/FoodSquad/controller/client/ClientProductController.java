package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientProductService;
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
 * Client-facing Product endpoints.
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
@RestController
@RequestMapping("/api/client/products")
@RequiredArgsConstructor
@Slf4j
public class ClientProductController {

    private final ClientProductService clientProductService;

    @Operation(summary = "List all products for storefront")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ClientProductDTO>> findAll() {
        log.info("Client request: list products");
        return ResponseEntity.ok(clientProductService.findAll());
    }

    @Operation(summary = "Get a product by ID for storefront")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientProductDTO> findById(@PathVariable UUID id) {
        log.info("Client request: get product id={}", id);
        return ResponseEntity.ok(clientProductService.getById(id));
    }

    @Operation(summary = "List products by category for storefront")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ClientProductDTO>> findByCategory(@PathVariable UUID categoryId) {
        log.info("Client request: list products by categoryId={}", categoryId);
        return ResponseEntity.ok(clientProductService.findByCategory(categoryId));
    }
}
