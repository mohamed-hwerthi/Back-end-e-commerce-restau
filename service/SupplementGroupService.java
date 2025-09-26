package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.dto.SupplementGroupDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing supplement groups.
 */
public interface SupplementGroupService {

    /**
     * Get all supplement groups.
     *
     * @return List of supplement groups.
     */
    List<SupplementGroupDTO> getAllSupplementGroups();

    /**
     * Create a new supplement group.
     *
     * @param supplementGroupDTO Supplement group data.
     * @return Created supplement group.
     */
    SupplementGroupDTO createSupplementGroup(SupplementGroupDTO supplementGroupDTO);

    /**
     * Delete a supplement group by ID.
     *
     * @param id Supplement group ID.
     */
    void deleteSupplementGroup(UUID id);

    /**
     * Get a supplement group by ID.
     *
     * @param id Supplement group ID.
     * @return Supplement group data.
     */
    SupplementGroupDTO getSupplementGroupById(UUID id);

    /**
     * Update a supplement group.
     *
     * @param id                 Supplement group ID.
     * @param supplementGroupDTO Supplement group data.
     * @return Updated supplement group.
     */
    SupplementGroupDTO updateSupplementGroup(UUID id, SupplementGroupDTO supplementGroupDTO);

    /**
     * Get supplement groups by option name.
     *
     * @param optionName Option name.
     * @return List of supplement groups.
     */
    List<SupplementGroupDTO> getSupplementGroupsByOption(String optionName);
}
