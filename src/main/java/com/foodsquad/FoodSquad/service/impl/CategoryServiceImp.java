package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CategoryMapper;
import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import com.foodsquad.FoodSquad.repository.CategoryRepository;
import com.foodsquad.FoodSquad.service.declaration.CategoryService;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MenuItemService menuItemService;

    public CategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper, @Lazy() MenuItemService menuItemService) {

        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.menuItemService = menuItemService;
    }

    @Override
    public PaginatedResponseDTO<CategoryDTO> findAllCategories(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryDTO> categoryDTOS = categories.getContent().stream().map(categoryMapper::toDto).toList();
        return new PaginatedResponseDTO<CategoryDTO>(categoryDTOS, categories.getTotalElements());

    }

    @Override
    public CategoryDTO findCategoryById(UUID id) {
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
    public CategoryDTO updateCategory(UUID id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        categoryMapper.updateExistedCatgoryFromDTO(categoryDTO, category);

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        category.getMenuItems().forEach(
                menuItem -> menuItemService.deleteMenuItem(menuItem.getId())
        );
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).toList();
    }

    @Override
    public List<Category> findCategoriesWithPromotions(Promotion promotion) {

        return  categoryRepository.findAllByPromotionsContaining(promotion) ;

    }

    @Override
    public Category findCategory(UUID categoryId) {
        return  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
       }


    @Override
    public Category saveCategory(Category category) {
         return    categoryRepository.save(category);
    }


    @Override
    public List<Category> saveCategories(List<Category> categories) {
        return  categoryRepository.saveAll(categories);
    }
}

