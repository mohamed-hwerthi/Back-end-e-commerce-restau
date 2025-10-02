package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.dto.LanguageDTO;
import com.foodsquad.FoodSquad.model.dto.StoreBasicDataDTO;
import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.model.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing Store-related operations.
 * Provides methods for creating, updating, retrieving, and deleting stores,
 * as well as fetching related data such as currency, language, and ownership.
 *
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
public interface StoreService {

    /**
     * Saves a new store.
     *
     * @param storeDTO the store data to be saved
     * @return the saved store as a DTO
     */
    StoreDTO save(StoreDTO storeDTO);

    /**
     * Updates an existing store.
     *
     * @param storeId  the ID of the store to update
     * @param storeDTO the updated store data
     * @return the updated store as a DTO
     */
    StoreDTO update(UUID storeId, StoreDTO storeDTO);

    /**
     * Retrieves all stores.
     *
     * @return a list of all stores as DTOs
     */
    List<StoreDTO> findAll();

    /**
     * Finds a store by its ID.
     *
     * @param id the ID of the store
     * @return the store as a DTO
     */
    StoreDTO findById(UUID id);

    /**
     * Deletes a store by its ID.
     *
     * @param id the ID of the store to delete
     */
    void delete(UUID id);

    /**
     * Finds a store by its owner.
     *
     * @param owner the user who owns the store
     * @return the store as a DTO
     */
    StoreDTO findByOwner(User owner);

    /**
     * Finds the store basic data by the current authenticated user email.
     *
     * @return store basic data DTO
     */
    StoreBasicDataDTO findByEmail();

    /**
     * Finds the store basic data by store slug.
     *
     * @param storeSlug the unique slug identifier of the store
     * @return store basic data DTO
     */
    StoreBasicDataDTO findByStoreSlug(String storeSlug);

    /**
     * Finds the currency associated with a store.
     *
     * @param storeId the store ID
     * @return the currency DTO of the store
     */
    CurrencyDTO findCurrencyOfStore(UUID storeId);

    /**
     * Finds the default language of a store.
     *
     * @param storeId the store ID
     * @return the default language DTO of the store
     */
    LanguageDTO findDefaultLanguageOfTheStore(UUID storeId);
}
