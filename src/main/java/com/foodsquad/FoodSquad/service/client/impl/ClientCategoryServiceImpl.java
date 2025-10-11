package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.mapper.MediaMapper;
import com.foodsquad.FoodSquad.mapper.client.ClientCategoryMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientCategoryDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.repository.CategoryRepository;
import com.foodsquad.FoodSquad.service.client.dec.ClientCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientCategoryServiceImpl implements ClientCategoryService {

    private final CategoryRepository categoryRepository;
    private final MediaMapper mediaMapper;
    private final ClientCategoryMapper clientCategoryMapper;

    @Override
    public List<ClientCategoryDTO> findAll() {
        log.info("Fetching all client categories");
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(clientCategoryMapper::toDto).toList();
    }

    @Override
    public ClientCategoryDTO findById(UUID id) {
        log.info("Fetching client category by id={}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return clientCategoryMapper.toDto(category);
    }


}
