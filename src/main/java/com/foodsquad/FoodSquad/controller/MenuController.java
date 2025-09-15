package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.MenuDTO;
import com.foodsquad.FoodSquad.service.declaration.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing menus in the Food Squad application.
 * Provides APIs for retrieving, creating, updating, and deleting menus.
 */
@Validated
@RestController
@RequestMapping("/api/menus")
@Tag(name = "8. Menu Management", description = "Menu Management API")
public class MenuController {

    private final MenuService menuService;

    /**
     * Constructor to initialize the MenuController with the required MenuService.
     *
     * @param menuService The service responsible for handling menu-related logic.
     */
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Retrieves a menu by its ID.
     *
     * @param id The ID of the menu to be retrieved.
     * @return A ResponseEntity containing the MenuDTO if found, or a 404 status if not found.
     */
    @Operation(summary = "Get a menu by its ID",
            description = "Fetches the menu based on the provided menu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Menu not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> getMenuById(
            @Parameter(description = "ID of the menu to be retrieved")
            @PathVariable Long id) {
        MenuDTO menuDTO = menuService.getMenuById(id);
        return ResponseEntity.ok(menuDTO);
    }

    /**
     * Retrieves a list of all available menus.
     *
     * @return A ResponseEntity containing a list of MenuDTO objects.
     */
    @Operation(summary = "Get all menus",
            description = "Fetches a list of all available menus.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menus retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class))))
    })
    @GetMapping("")
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        List<MenuDTO> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    /**
     * Creates a new menu based on the provided details.
     *
     * @param menuDTO The MenuDTO containing the details of the menu to be created.
     * @return A ResponseEntity containing the created MenuDTO with status 201.
     */
    @Operation(summary = "Create a new menu",
            description = "Creates a new menu from the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<MenuDTO> createMenu(
            @Parameter(description = "Menu details to be created", required = true)
            @RequestBody MenuDTO menuDTO) {
        MenuDTO createdMenu = menuService.createMenu(menuDTO);
        return ResponseEntity.status(201).body(createdMenu);
    }

    /**
     * Updates an existing menu with the provided details.
     *
     * @param id      The ID of the menu to be updated.
     * @param menuDTO The updated menu details.
     * @return A ResponseEntity containing the updated MenuDTO.
     */
    @Operation(summary = "Update an existing menu",
            description = "Updates the menu with the given ID using the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu updated successfully"),
            @ApiResponse(responseCode = "404", description = "Menu not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> updateMenu(
            @Parameter(description = "ID of the menu to be updated")
            @PathVariable Long id,
            @Parameter(description = "Updated menu details")
            @RequestBody MenuDTO menuDTO) {
        MenuDTO updatedMenu = menuService.updateMenu(id, menuDTO);
        return ResponseEntity.ok(updatedMenu);
    }

    /**
     * Deletes a menu by its ID.
     *
     * @param id The ID of the menu to be deleted.
     * @return A ResponseEntity with status 204 (No Content) upon successful deletion.
     */
    @Operation(summary = "Delete a menu by its ID",
            description = "Deletes the menu based on the provided menu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Menu not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(
            @Parameter(description = "ID of the menu to be deleted")
            @PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
