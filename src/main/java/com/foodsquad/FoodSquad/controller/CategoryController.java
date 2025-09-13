package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.service.declaration.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@Tag(name = "7. Category Management", description = "Category Management API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> findAllCategories() {
        log.info("Fetching all categories without pagination");
        List<CategoryDTO> categories = categoryService.findAllCategories();
        log.info("Found {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/pageable")
    public ResponseEntity<PaginatedResponseDTO<CategoryDTO>> findAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("Fetching categories with pagination - page: {}, limit: {}", page, limit);
        PaginatedResponseDTO<CategoryDTO> response = categoryService.findAllCategories(page, limit);
        log.info("Returning {} categories out of total {}", response.getItems().size(), response.getTotalCount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable UUID id) {
        log.info("Fetching category by ID: {}", id);
        CategoryDTO category = categoryService.findCategoryById(id);
        log.info("Found category: {}", category != null ? category.getName() : "null");
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getName());
        if (categoryDTO.getId() != null && StringUtils.hasText(categoryDTO.getId().toString())) {
            log.warn("Attempted to create category with non-null ID: {}", categoryDTO.getId());
            throw new IllegalArgumentException("Category ID must be null in creation mode");
        }
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        log.info("Category created with ID: {}", createdCategory.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        log.info("Updating category ID: {} with new data: {}", id, categoryDTO.getName());
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        log.info("Category updated: {}", updatedCategory.getId());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        log.info("Deleting category with ID: {}", id);
        categoryService.deleteCategory(id);
        log.info("Category deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PaginatedResponseDTO<CategoryDTO>> searchCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String searchTerm
    ) {
        log.info("Searching categories with term: '{}', page: {}, limit: {}", searchTerm, page, limit);
        PaginatedResponseDTO<CategoryDTO> response = categoryService.findCategoriesByPageAndSearch(page, limit, searchTerm);
        log.info("Found {} categories matching search term '{}'", response.getItems().size(), searchTerm);
        return ResponseEntity.ok(response);
    }
}
