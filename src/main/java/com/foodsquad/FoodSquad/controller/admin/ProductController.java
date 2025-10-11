package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.ProductFilterByCategoryAndQueryRequestDTO;
import com.foodsquad.FoodSquad.service.admin.dec.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/products")
@Slf4j
@Tag(name = "5.Product Management", description = "Menu Item Management API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create a new menu item",
            description = "Creates a new menu item with the provided details."
    )
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("Request to create menu item: {}", productDTO);

        ProductDTO createdProduct = productService.createProduct(productDTO);

        log.info("Menu item created successfully: {}", createdProduct.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Get a menu item by ID", description = "Retrieve a menu item by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID of the menu item to retrieve", example = "1")
            @PathVariable UUID id) {
        log.debug("Request to get menu item by id: {}", id);
        ProductDTO product = productService.getProductById(id);
        log.info("Fetched menu item: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }



    @Operation(summary = "Update a menu item by ID", description = "Update the details of an existing menu item by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID of the menu item to update", example = "1")
            @PathVariable UUID id,
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Request to update menu item: {}", id);
        ResponseEntity<ProductDTO> response = productService.updateProduct(id, productDTO);
        log.info("Menu item updated successfully: {}", id);
        return response;
    }

    @Operation(summary = "Delete a menu item by ID", description = "Delete an existing menu item by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(
            @Parameter(description = "ID of the menu item to delete", example = "1")
            @PathVariable UUID id) {
        log.warn("Request to delete menu item: {}", id);
        ResponseEntity<Map<String, String>> response = productService.deleteProduct(id);
        log.info("Menu item deleted successfully: {}", id);
        return response;
    }


    @Operation(summary = "Delete menu items by IDs", description = "Delete existing menu items by their unique IDs.")
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, String>> deleteProductsByIds(
            @Parameter(description = "List of IDs of the menu items to delete", example = "[1, 2, 3]")
            @RequestParam List<UUID> ids) {
        log.warn("Request to delete menu items by ids: {}", ids);
        ResponseEntity<Map<String, String>> response = productService.deleteProductsByIds(ids);
        log.info("Menu items deleted successfully. Count: {}", ids.size());
        return response;
    }

    @Operation(summary = "Search menu items by query , stock or not in stock   and category   ", description = "Retrieve a list of menu items that their title  match the provided query  and matchs a categories.")
    @PostMapping("/search/by-query-categories")
    public ResponseEntity<PaginatedResponseDTO<ProductDTO>> searchProductsByQuery(@RequestBody() ProductFilterByCategoryAndQueryRequestDTO ProductFilterByCategoryAndQueryRequestDTO, Pageable pageable) {
        log.debug("Request to search menu items by query & categories with payload: {} and pageable: {}", ProductFilterByCategoryAndQueryRequestDTO, pageable);
        PaginatedResponseDTO<ProductDTO> paginatedResponseDTOS = productService.searchProductsByQuery(ProductFilterByCategoryAndQueryRequestDTO, pageable);
        log.info("Search by query & categories completed successfully");
        return ResponseEntity.status(HttpStatus.OK).body(paginatedResponseDTOS);
    }

    @Operation(summary = "Search menu items by query", description = "Retrieve a list of menu items that their title  match the provided query.")
    @GetMapping("/search/{query}")
    public ResponseEntity<PaginatedResponseDTO<ProductDTO>> searchProductsByQuery(@Parameter(description = "Query to search menu items by title", example = "pizza") @PathVariable("query") String query, Pageable pageable) {
        log.debug("Request to search menu items by query: '{}' with pageable: {}", query, pageable);
        PaginatedResponseDTO<ProductDTO> paginatedResponseDTOS = productService.searchProductsByQuery(query, pageable);
        log.info("Search by query '{}' completed successfully", query);
        return ResponseEntity.status(HttpStatus.OK).body(paginatedResponseDTOS);
    }


    @Operation(summary = "find Menu item by its qr code ", description = "find Menu item by its qr code ")
    @GetMapping("/bar-code/{barCode}")
    public ResponseEntity<ProductDTO> findByQrCode(@Parameter(description = "Search by bar code  ", example = "0001236") @PathVariable("barCode") String barCode) {
        log.debug("Request to find menu item by barcode: {}", barCode);
        ProductDTO dto = productService.findByBarCode(barCode);
        log.info("Found menu item for barcode: {}", barCode);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete a media from a menu item",
            description = "Deletes a specific media associated with a menu item. This will also delete the related file from storage."
    )
    @DeleteMapping("/{ProductId}/media/{mediaId}")
    public ResponseEntity<Map<String, String>> deleteMediaFromProduct(
            @Parameter(description = "ID of the menu item", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("ProductId") UUID productId,

            @Parameter(description = "ID of the media to delete", example = "1")
            @PathVariable UUID mediaId) {
        log.info("Request to delete media {} from menu item {}", mediaId, productId);
        productService.deleteMediaForProduct(productId, mediaId);
        log.info("Media {} deleted successfully from menu item {}", mediaId, productId);
        return ResponseEntity.ok(Map.of("message", "Media " + mediaId + " deleted successfully from menu item " + productId));
    }

    /**
     * Get all products that are marked as options (isOption = true).
     *
     * @return ResponseEntity containing a list of ProductDTOs
     */
    @GetMapping("/option-products")
    @Operation(
            summary = "Fetch all option products",
            description = "Returns all products that are marked as options (isOption = true).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved option products",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ProductDTO>> getAllProductOptions() {
        log.debug("Received request to fetch all option products");

        List<ProductDTO> optionProducts = productService.getAllProductOptions();

        if (optionProducts.isEmpty()) {
            log.info("No option products found in the database");
        } else {
            log.info("Returning {} option products", optionProducts.size());
        }

        return ResponseEntity.ok(optionProducts);
    }


}
