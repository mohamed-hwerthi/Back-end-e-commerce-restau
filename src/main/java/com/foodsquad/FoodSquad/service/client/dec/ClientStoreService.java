package com.foodsquad.FoodSquad.service.client.dec;


import com.foodsquad.FoodSquad.model.dto.client.ClientStoreDTO;
import java.util.UUID;

/**
 * Service interface for retrieving client-facing store information.
 * This service exposes only public store data for the storefront.
 *
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
public interface ClientStoreService {

    /**
     * Retrieves a store by its ID for client-facing purposes.
     *
     * @param storeId the UUID of the store
     * @return ClientStoreDTO containing public store information
     */
    ClientStoreDTO getStoreInformation();
}