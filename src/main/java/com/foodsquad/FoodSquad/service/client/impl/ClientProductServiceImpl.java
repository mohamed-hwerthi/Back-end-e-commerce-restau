
package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.service.client.dec.ClientProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientProductServiceImpl implements ClientProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ClientProductDTO getById(UUID id) {
        log.info("Fetching client product by id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return (productMapper.toClientProductDTO(product));
    }

    @Override
    public PaginatedResponseDTO<ProductDTO> getAllProducts(
            int page,
            int limit,
            String sortBy,
            boolean desc,
            UUID categoryId,
            String isDefault,
            String priceSortDirection
    ) {
        log.debug("Fetching products with filters - page: {}, limit: {}, sortBy: {}, desc: {}, categoryId: {}, isDefault: {}, priceSortDirection: {}",
                page, limit, sortBy, desc, categoryId, isDefault, priceSortDirection);

        // Determine sorting
        Sort sort;

        if (priceSortDirection != null && !priceSortDirection.isBlank()) {
            Sort.Direction priceDirection = priceSortDirection.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sort = Sort.by(priceDirection, "price");
            log.debug("Sorting by price in {} order", priceDirection);
        }
        else {
            String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
            Sort.Direction direction = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortField);
            log.debug("Sorting by field '{}' in {} order", sortField, direction);
        }

        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<Product> productPage;

        if (categoryId != null) {
            log.debug("Filtering products by category ID: {}", categoryId);
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            log.debug("Fetching all products without category filter");
            productPage = productRepository.findAll(pageable);
        }

        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(productMapper::toDto)
                .toList();

        log.debug("Fetched {} products, total elements: {}", productDTOs.size(), productPage.getTotalElements());

        return new PaginatedResponseDTO<>(productDTOs, productPage.getTotalElements());
    }

    @Override
    public List<ClientProductDTO> findByCategory(UUID categoryId) {
          return null  ;
    }
}
