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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Implementation of {@link CategoryService}.
 */
@Slf4j
@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final MenuItemService menuItemService;

    public CategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper, MenuItemService menuItemService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.menuItemService = menuItemService;
    }


    @Override
    public PaginatedResponseDTO<CategoryDTO> findAllCategories(int page, int limit) {
        log.info("Fetching categories with pagination - page: {}, limit: {}", page, limit);
        Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryDTO> categoryDTOs = categories.map(categoryMapper::toDto).toList();
        log.debug("Fetched {} categories", categoryDTOs.size());
        return new PaginatedResponseDTO<>(categoryDTOs, categories.getTotalElements());
    }

    @Override
    public CategoryDTO findCategoryById(UUID id) {
        log.info("Finding category by id: {}", id);
        Category category = findCategory(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getName());
        Category category = categoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        log.debug("Created category with id: {}", savedCategory.getId());
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(UUID id, CategoryDTO categoryDTO) {
        log.info("Updating category with id: {}", id);
        Category category = findCategory(id);
        categoryMapper.updateExistedCatgoryFromDTO(categoryDTO, category);
        Category updatedCategory = categoryRepository.save(category);
        log.debug("Updated category: {}", updatedCategory);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(UUID id) {
        log.warn("Deleting category with id: {}", id);
        Category category = findCategory(id);
        if (category.getMenuItems() != null && !category.getMenuItems().isEmpty()) {
            log.info("Deleting {} menu items linked to category {}", category.getMenuItems().size(), id);
            category.getMenuItems().forEach(menuItem -> menuItemService.deleteMenuItem(menuItem.getId()));
        }

        categoryRepository.delete(category);
        log.info("Deleted category with id: {}", id);
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        log.info("Fetching all categories without pagination");
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public List<Category> findCategoriesWithPromotions(Promotion promotion) {
        log.info("Finding categories with promotion: {}", promotion.getId());
        return categoryRepository.findAllByPromotionsContaining(promotion);
    }

    @Override
    public Category findCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with id: {}", categoryId);
                    return new EntityNotFoundException("Category not found with id: " + categoryId);
                });
    }

    @Override
    public Category saveCategory(Category category) {
        log.info("Saving category: {}", category.getName());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> saveCategories(List<Category> categories) {
        log.info("Saving {} categories", categories.size());
        return categoryRepository.saveAll(categories);
    }


    @Override
    public PaginatedResponseDTO<CategoryDTO> findCategoriesByPageAndSearch(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());

        Page<Category> categoryPage;
        if (StringUtils.hasText(searchTerm)) {
            categoryPage = categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm, pageable);
        } else {
            categoryPage = categoryRepository.findAll(pageable);
        }

        List<CategoryDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                categoryDTOs,
                categoryPage.getTotalPages()
        );
    }


}
