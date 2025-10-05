package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for retrieving client-facing product information.
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
public interface ClientProductService {

    ClientProductDTO getById(UUID id);

    PaginatedResponseDTO<ProductDTO> getAllProducts(int page, int limit , String query ,
                                                     UUID categoryId,
                                                    String priceSortDirection);
    List<ClientProductDTO> findByCategory(UUID categoryId);
}
