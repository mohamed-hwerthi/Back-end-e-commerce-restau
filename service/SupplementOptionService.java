package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.dto.SupplementOptionDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing supplement options.
 */
public interface SupplementOptionService {

    /**
     * Get all supplement options.
     *
     * @return List of supplement options.
     */
    List<SupplementOptionDTO> getAllSupplementOptions();

    /**
     * Create a new supplement option.
     *
     * @param supplementOptionDTO Supplement option data.
     * @return Created supplement option.
     */
    SupplementOptionDTO createSupplementOption(SupplementOptionDTO supplementOptionDTO);

    /**
     * Delete a supplement option by ID.
     *
     * @param id Supplement option ID.
     */
    void deleteSupplementOption(UUID id);
}
