package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "5. Menu Item Management", description = "Menu Item Management API")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {

        this.menuItemService = menuItemService;
    }

    @Operation(summary = "Create a new menu item", description = "Create a new menu item with the provided details.")
    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) {

         return menuItemService.createMenuItem(menuItemDTO);
    }

    @Operation(summary = "Get a menu item by ID", description = "Retrieve a menu item by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(
            @Parameter(description = "ID of the menu item to retrieve", example = "1")
            @PathVariable Long id) {

        return menuItemService.getMenuItemById(id);
    }

    @Operation(summary = "Get all menu items", description = "Retrieve a list of menu items with optional filters and sorting.")
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<MenuItemDTO>> getAllMenuItems(
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int limit,

            @Parameter(description = "Sort by field, e.g., 'salesCount' or 'price'", example = "salesCount", required = false)
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Sort direction: true for descending, false for ascending")
            @RequestParam(required = false) boolean desc,

            @Parameter(description = "Filter by categoryId , params is named categoryFilter but it contains the category Id '", required = false)
            @RequestParam(required = false) Long categoryFilter,

            @Parameter(description = "Filter by default status: 'true' for default items, 'false' for non-default items", required = false)
            @RequestParam(required = false) String isDefault,

            @Parameter(description = "Sort direction for price: 'asc' for ascending, 'desc' for descending", required = false)
            @RequestParam(required = false) String priceSortDirection) {
//        return menuItemService.getAllMenuItems(page, limit, sortBy, desc, categoryFilter, isDefault, priceSortDirection);
        PaginatedResponseDTO<MenuItemDTO> response = menuItemService.getAllMenuItems(page, limit, sortBy, desc, categoryFilter, isDefault, priceSortDirection);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a menu item by ID", description = "Update the details of an existing menu item by its unique ID.")
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
            @Parameter(description = "ID of the menu item to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MenuItemDTO menuItemDTO) {

        return menuItemService.updateMenuItem(id, menuItemDTO);
    }

    @Operation(summary = "Delete a menu item by ID", description = "Delete an existing menu item by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMenuItem(
            @Parameter(description = "ID of the menu item to delete", example = "1")
            @PathVariable Long id) {

        return menuItemService.deleteMenuItem(id);
    }

    @Operation(summary = "Get menu items by IDs", description = "Retrieve a list of menu items by their unique IDs.")
    @GetMapping("/batch")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByIds(
            @Parameter(description = "List of IDs of the menu items to retrieve", example = "[1, 2, 3]")
            @RequestParam List<Long> ids) {

        return menuItemService.getMenuItemsByIds(ids);
    }

    @Operation(summary = "Delete menu items by IDs", description = "Delete existing menu items by their unique IDs.")
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, String>> deleteMenuItemsByIds(
            @Parameter(description = "List of IDs of the menu items to delete", example = "[1, 2, 3]")
            @RequestParam List<Long> ids) {

        return menuItemService.deleteMenuItemsByIds(ids);
    }

    @Operation(summary = "Search menu items by query", description = "Retrieve a list of menu items that their title  match the provided query.")
    @GetMapping("/search/{query}")
    public ResponseEntity<PaginatedResponseDTO<MenuItemDTO>> searchMenuItemsByQuery(@Parameter(description = "Query to search menu items by title", example = "pizza") @PathVariable("query") String query, Pageable pageable) {

        PaginatedResponseDTO<MenuItemDTO> paginatedResponseDTOS = menuItemService.searchMenuItemsByQuery(query, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(paginatedResponseDTOS);
    }

    @Operation(summary = "find Menu item by its qr code ", description = "find Menu item by its qr code ")
    @GetMapping("/bar-code/{barCode}")
    public ResponseEntity<MenuItemDTO> findByQrCode(@Parameter(description = "Search by bar code  ", example = "0001236") @PathVariable("barCode") String barCode) {

        return ResponseEntity.ok(menuItemService.findByBarCode(barCode));
    }

}
