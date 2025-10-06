package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductListDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(
            summary = "Get all products for storefront",
            description = "Retrieve paginated products with optional search, category filtering, and price sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ProductDTO>> getAllProducts(
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int limit,

            @Parameter(description = "Search query to filter products by name or description", example = "pizza", required = false)
            @RequestParam(required = false) String query,

            @Parameter(description = "Filter by category ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam(required = false, name = "categoryFilter") UUID categoryFilter,

            @Parameter(description = "Sort direction for price: 'asc' for ascending, 'desc' for descending", example = "asc")
            @RequestParam(required = false) String priceSortDirection
    ) {
        log.debug(
                "Request to get products: page={}, limit={}, query={}, categoryFilter={}, priceSortDirection={}",
                page, limit, query, categoryFilter, priceSortDirection
        );

        PaginatedResponseDTO<ProductDTO> response = clientProductService.getAllProducts(
                page,
                limit,
                query,
                categoryFilter,
                priceSortDirection
        );

        log.info("Fetched {} products successfully (page={}, limit={})",
                response.getItems().size(), page, limit);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a product by ID for storefront")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientProductListDTO> findById(@PathVariable UUID id) {
        log.info("Client request: get product id={}", id);
        return ResponseEntity.ok(clientProductService.getById(id));
    }

    @Operation(summary = "List products by category for storefront")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ClientProductListDTO>> findByCategory(@PathVariable UUID categoryId) {
        log.info("Client request: list products by categoryId={}", categoryId);
        return ResponseEntity.ok(clientProductService.findByCategory(categoryId));
    }
}
