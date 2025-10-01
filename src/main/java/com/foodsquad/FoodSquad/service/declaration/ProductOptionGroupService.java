package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing supplement groups.
 */
public interface ProductOptionGroupService {

    /**
     * Get all supplement groups.
     *
     * @return List of supplement groups.
     */
    List<ProductOptionGroupDTO> getAllProductOptionGroups();

    /**
     * Create a new supplement group.
     *
     * @param supplementGroupDTO Supplement group data.
     * @return Created supplement group.
     */
    ProductOptionGroupDTO createProductOptionGroup(ProductOptionGroupDTO supplementGroupDTO);

    /**
     * Delete a supplement group by ID.
     *
     * @param id Supplement group ID.
     */
    void deleteProductOptionGroup(UUID id);

    /**
     * Get a supplement group by ID.
     *
     * @param id Supplement group ID.
     * @return Supplement group data.
     */
    ProductOptionGroupDTO getProductOptionGroupById(UUID id);

    /**
     * Update a supplement group.
     *
     * @param id                 Supplement group ID.
     * @param supplementGroupDTO Supplement group data.
     * @return Updated supplement group.
     */
    ProductOptionGroupDTO updateProductOptionGroup(UUID id, ProductOptionGroupDTO supplementGroupDTO);


}
