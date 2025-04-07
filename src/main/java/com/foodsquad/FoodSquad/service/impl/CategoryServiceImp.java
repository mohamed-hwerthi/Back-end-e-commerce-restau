package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CategoryMapper;
import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.repository.CategoryRepository;
import com.foodsquad.FoodSquad.service.declaration.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  CategoryServiceImp implements CategoryService {

    private   final   CategoryRepository categoryRepository;
    private   final   CategoryMapper categoryMapper;

    public CategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {

        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public PaginatedResponseDTO<CategoryDTO> findAllCategories(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
         Page<Category>categories =  categoryRepository.findAll(pageable);
         List<CategoryDTO>categoryDTOS  = categories.getContent().stream().map(categoryMapper::toDto).toList();
         return  new PaginatedResponseDTO<CategoryDTO>(categoryDTOS , categories.getTotalElements()) ;

    }

    @Override
    public CategoryDTO findCategoryById(Long id) {
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

    @Override
    public List<CategoryDTO> findAllCategories() {
        return    categoryRepository.findAll().stream().map(categoryMapper::toDto).toList() ;
    }

}

