package com.foodsquad.FoodSquad.controller;

import com.ctc.wstx.util.StringUtil;
import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.service.declaration.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/categories")
@Tag(name = "7. Category Management", description = "Category Management API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories   ", description = "Retrieve all categories available in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class))))
    })
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> findAllCategories() {
        return ResponseEntity.ok().body(categoryService.findAllCategories());
    }

    @Operation(summary = "Get all categories  with pagination ", description = "Retrieve all categories  with pagination.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class))))
    })
    @GetMapping("/pageable")
    public ResponseEntity<PaginatedResponseDTO<CategoryDTO>> findAllCategories(@Parameter(description = "Page number, starting from 0", example = "0")
                                                                               @RequestParam(defaultValue = "0") int page,

                                                                               @Parameter(description = "Number of items per page", example = "10")
                                                                               @RequestParam(defaultValue = "10") int limit
    ) {

        return ResponseEntity.ok(categoryService.findAllCategories(page, limit));
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findCategoryById(
            @Parameter(description = "ID of the category to be retrieved", required = true) @PathVariable UUID id) {
        CategoryDTO category = categoryService.findCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Create a new category", description = "Add a new category to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @Parameter(description = "Category details to be created", required = true) @RequestBody CategoryDTO categoryDTO) {
        if(  categoryDTO.getId() != null && StringUtils.hasText(categoryDTO.getId().toString())){
            throw new IllegalArgumentException("Category ID must be null in creation mode ");
        }
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @Operation(summary = "Update category by ID", description = "Modify the details of an existing category using its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @Parameter(description = "ID of the category to be updated", required = true) @PathVariable UUID id,
            @Parameter(description = "Updated category details", required = true) @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "Delete category by ID", description = "Remove a category from the system using its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to be deleted", required = true) @PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}
