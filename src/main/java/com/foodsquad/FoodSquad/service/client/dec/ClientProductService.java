package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;

import java.util.UUID;

/**
 * Service interface for retrieving client-facing product information.
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
public interface ClientProductService {

    ClientProductDTO getById(UUID id);

    PaginatedResponseDTO<ClientProductDTO> searchProducts(int page, int limit, String query,
                                                    UUID categoryId,
                                                    String priceSortDirection);

}
