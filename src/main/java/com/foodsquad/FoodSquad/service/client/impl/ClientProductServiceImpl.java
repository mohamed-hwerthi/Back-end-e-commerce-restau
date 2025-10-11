package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.mapper.client.ClientProductMapper;
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

    private final ClientProductMapper clientproductMapper;

    private final LocaleContext localeContext;


    @Override
    public ClientProductDTO getById(UUID id) {
        log.info("Fetching client product by id={}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return clientproductMapper.toDto(product);
    }

    @Override
    public PaginatedResponseDTO<ClientProductDTO> searchProducts(
            int page,
            int limit,
            String query,
            UUID categoryId,
            String priceSortDirection
    ) {
        log.debug("Fetching products - page={}, limit={}, query={}, categoryId={}, priceSortDirection={}, lang={}",
                page, limit, query, categoryId, priceSortDirection);

        Sort sort = Sort.unsorted();

        if (priceSortDirection != null && !priceSortDirection.isBlank()) {
            Sort.Direction direction = priceSortDirection.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sort = Sort.by(direction, "price");
        }
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> productPage;

        boolean hasQuery = query != null && !query.isBlank();
        boolean hasCategory = categoryId != null;

        if (hasQuery && hasCategory) {
            productPage = productRepository.searchByJsonLanguageAndCategory(query, localeContext.getLocale(), categoryId, pageable);
        } else if (hasQuery) {
            productPage = productRepository.searchByJsonLanguage(query, localeContext.getLocale(), pageable);
        } else if (hasCategory) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ClientProductDTO> productDTOs = productPage.getContent().stream()
                .map(clientproductMapper::toDto)
                .toList();

        return new PaginatedResponseDTO<>(productDTOs, productPage.getTotalElements());
    }


}
