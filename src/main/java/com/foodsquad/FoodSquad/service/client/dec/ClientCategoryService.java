package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.client.ClientCategoryDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for retrieving client-facing category information.
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
public interface ClientCategoryService {

    List<ClientCategoryDTO> findAll();

    ClientCategoryDTO findById(UUID id);
}
