package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Promotion;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing categories.
 * Provides methods for CRUD operations and querying categories with promotions.
 */
public interface CategoryService {

    /**
     * Retrieve paginated list of categories.
     *
     * @param page  The page number (starting from 0 or 1 based on implementation).
     * @param limit The maximum number of categories per page.
     * @return Paginated response DTO containing category DTOs.
     */
    PaginatedResponseDTO<CategoryDTO> findAllCategories(int page, int limit);

    /**
     * Find a category by its unique identifier.
     *
     * @param id UUID of the category.
     * @return CategoryDTO representing the category if found.
     */
    CategoryDTO findCategoryById(UUID id);

    /**
     * Create a new category.
     *
     * @param categoryDTO Data transfer object containing category details.
     * @return The created CategoryDTO with assigned ID and other generated fields.
     */
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    /**
     * Update an existing category identified by UUID.
     *
     * @param id          UUID of the category to update.
     * @param categoryDTO Data transfer object with updated data.
     * @return The updated CategoryDTO.
     */
    CategoryDTO updateCategory(UUID id, CategoryDTO categoryDTO);

    /**
     * Delete a category by its unique identifier.
     *
     * @param id UUID of the category to delete.
     */
    void deleteCategory(UUID id);

    /**
     * Retrieve all categories without pagination.
     *
     * @return List of CategoryDTOs representing all categories.
     */
    List<CategoryDTO> findAllCategories();

    /**
     * Find all categories that have the specified promotion.
     *
     * @param promotion Promotion entity for filtering categories.
     * @return List of Category entities associated with the promotion.
     */
    List<Category> findCategoriesWithPromotions(Promotion promotion);

    /**
     * Find category entity by its unique identifier.
     *
     * @param categoryId UUID of the category entity.
     * @return Category entity if found.
     */
    Category findCategory(UUID categoryId);

    /**
     * Save (insert or update) a category entity.
     *
     * @param category Category entity to save.
     * @return The saved Category entity.
     */
    Category saveCategory(Category category);

    /**
     * Save a list of category entities.
     *
     * @param categories List of Category entities to save.
     * @return List of saved Category entities.
     */
    List<Category> saveCategories(List<Category> categories);

    /**
     * Retrieve a paginated list of categories with optional search/filter.
     *
     * @param page       The page number (starting from 0 or 1 based on implementation)
     * @param limit      The number of categories per page
     * @param searchTerm Optional search term to filter categories by name or description
     * @return PaginatedResponseDTO containing CategoryDTOs
     */
    PaginatedResponseDTO<CategoryDTO> findCategoriesByPageAndSearch(int page, int limit, String searchTerm);

}
