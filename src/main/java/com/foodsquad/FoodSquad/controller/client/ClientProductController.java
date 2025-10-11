package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @Operation(summary = "Search and paginate client products")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ClientProductDTO>> searchProducts(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int limit,

            @Parameter(description = "Free-text search query", example = "pizza")
            @RequestParam(required = false) String query,

            @Parameter(description = "Filter by category ID", example = "b58a4c23-8ddf-4bc3-8b6e-4c3d01a5b9a3")
            @RequestParam(name = "categoryFilter", required = false) UUID categoryId,

            @Parameter(description = "Sort by price direction (asc or desc)", example = "asc")
            @RequestParam(required = false) String priceSortDirection
    ) {
        log.info("Client request: search products page={}, limit={}, query={}, category={}, sort={}",
                page, limit, query, categoryId, priceSortDirection);

        PaginatedResponseDTO<ClientProductDTO> response = clientProductService.searchProducts(
                page,
                limit,
                query,
                categoryId,
                priceSortDirection
        );

        return ResponseEntity.ok(response);
    }


}
