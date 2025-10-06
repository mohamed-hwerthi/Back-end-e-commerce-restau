package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDetailDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductListDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for retrieving client-facing product information.
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
public interface ClientProductService {

    ClientProductDetailDTO getById(UUID id);

    PaginatedResponseDTO<ProductDTO> getAllProducts(int page, int limit , String query ,
                                                     UUID categoryId,
                                                    String priceSortDirection);
    List<ClientProductListDTO> findByCategory(UUID categoryId);
}
