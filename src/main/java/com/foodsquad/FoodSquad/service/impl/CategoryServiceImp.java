package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CategoryMapper;
import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.repository.CategoryRepository;
import com.foodsquad.FoodSquad.service.declaration.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  CategoryServiceImp implements CategoryService {

    private  CategoryRepository categoryRepository;
    private  CategoryMapper categoryMapper;

    public CategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {

        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDTOList(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}

