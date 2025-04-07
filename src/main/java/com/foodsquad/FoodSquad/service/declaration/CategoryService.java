package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;

import java.util.List;

public interface CategoryService {
    PaginatedResponseDTO<CategoryDTO> findAllCategories(int page, int limit);

    CategoryDTO findCategoryById(Long id);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);
    List<CategoryDTO>findAllCategories()  ;



}
