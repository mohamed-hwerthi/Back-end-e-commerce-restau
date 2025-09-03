package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Promotion;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    PaginatedResponseDTO<CategoryDTO> findAllCategories(int page, int limit);

    CategoryDTO findCategoryById(UUID id);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(UUID id, CategoryDTO categoryDTO);

    void deleteCategory(UUID id);

    List<CategoryDTO> findAllCategories();

    List<Category> findCategoriesWithPromotions(Promotion promotion);


    Category  findCategory(UUID categoryId);

    Category saveCategory(Category  category);

    List<Category>saveCategories(List<Category>categories);


}
