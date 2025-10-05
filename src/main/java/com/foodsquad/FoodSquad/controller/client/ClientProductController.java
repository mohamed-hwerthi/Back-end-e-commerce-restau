package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
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

    @Operation(summary = "Get all menu items", description = "Retrieve a list of menu items with optional filters and sorting.")
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<ProductDTO>> getAllProducts(
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int limit,

            @Parameter(description = "Sort by field, e.g., 'salesCount' or 'price'", example = "salesCount", required = false)
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Sort direction: true for descending, false for ascending")
            @RequestParam(required = false) boolean desc,

            @Parameter(description = "Filter by categoryId , params is named categoryFilter but it contains the category Id '", required = false)
            @RequestParam(required = false) UUID categoryFilter,

            @Parameter(description = "Filter by default status: 'true' for default items, 'false' for non-default items", required = false)
            @RequestParam(required = false) String isDefault,

            @Parameter(description = "Sort direction for price: 'asc' for ascending, 'desc' for descending", required = false)
            @RequestParam(required = false) String priceSortDirection
    ) {
        log.debug("Request to get all menu items: page={}, limit={}, sortBy={}, desc={}, categoryFilter={}, isDefault={}, priceSortDirection={}",
                page, limit, sortBy, desc, categoryFilter, isDefault, priceSortDirection);
        PaginatedResponseDTO<ProductDTO> response = clientProductService.getAllProducts(page, limit, sortBy, desc, categoryFilter, isDefault, priceSortDirection);
        log.info("Fetched menu items page {} with limit {} successfully", page, limit);
        return ResponseEntity.ok(response);
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
