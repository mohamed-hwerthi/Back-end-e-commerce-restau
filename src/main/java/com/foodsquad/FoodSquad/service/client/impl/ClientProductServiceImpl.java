package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.mapper.MediaMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientCategoryDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.repository.CategoryRepository;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.service.client.dec.ClientProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientProductServiceImpl implements ClientProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MediaMapper mediaMapper;

    @Override
    public ClientProductDTO getById(UUID id) {
        log.info("Fetching client product by id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toClientDto(product);
    }

    @Override
    public List<ClientProductDTO> findAll() {
        log.info("Fetching all client products");
        return productRepository.findAll().stream().map(this::toClientDto).toList();
    }

    @Override
    public List<ClientProductDTO> findByCategory(UUID categoryId) {
        log.info("Fetching client products by categoryId={}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return productRepository.findAllByCategoriesContaining(category)
                .stream().map(this::toClientDto).toList();
    }

    private ClientProductDTO toClientDto(Product product) {
        return ClientProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .inStock(product.getQuantity() > 0)
                .medias(product.getMedias() != null ? product.getMedias().stream().map(mediaMapper::toDto).toList() : List.of())
                .categories(product.getCategories() != null ? product.getCategories().stream().map(this::toClientCategoryDto).toList() : List.of())
                .build();
    }

    private ClientCategoryDTO toClientCategoryDto(Category category) {
        return ClientCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .medias(category.getMedias() != null ? category.getMedias().stream().map(mediaMapper::toDto).toList() : List.of())
                .build();
    }
}
