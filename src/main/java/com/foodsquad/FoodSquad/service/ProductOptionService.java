package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.dto.ProductOptionDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing supplement options.
 */
public interface ProductOptionService {

    /**
     * Get all supplement options.
     *
     * @return List of supplement options.
     */
    List<ProductOptionDTO> getAllProductOptions();

    /**
     * Create a new supplement option.
     *
     * @param supplementOptionDTO Supplement option data.
     * @return Created supplement option.
     */
    ProductOptionDTO createProductOption(ProductOptionDTO supplementOptionDTO);

    /**
     * Delete a supplement option by ID.
     *
     * @param id Supplement option ID.
     */
    void deleteProductOption(UUID id);
}
