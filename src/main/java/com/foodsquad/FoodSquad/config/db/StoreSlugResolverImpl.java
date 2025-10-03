package com.foodsquad.FoodSquad.config.db;

import com.foodsquad.FoodSquad.model.entity.Store;
import com.foodsquad.FoodSquad.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link StoreSlugResolver} that resolves a tenant identifier
 * from a given store slug.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Looking up a {@link Store} entity using the slug provided.</li>
 *   <li>Transforming the {@link Store} ID into a database-safe tenant identifier
 *       (replacing dashes with underscores).</li>
 *   <li>Returning the tenant name in the format {@code tenant_<storeId>}.</li>
 * </ul>
 *
 * Example:
 * <pre>
 *     slug = "my-store"
 *     storeId = "123e4567-e89b-12d3-a456-426614174000"
 *     resolved tenant = "tenant_123e4567_e89b_12d3_a456_426614174000"
 * </pre>
 */
@Component
public class StoreSlugResolverImpl implements StoreSlugResolver {

    private final StoreRepository storeRepository;

    /**
     * Constructs a new {@code StoreSlugResolverImpl}.
     *
     * @param storeRepository the repository used to fetch {@link Store} entities
     */
    @Autowired
    public StoreSlugResolverImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Resolves the tenant identifier from the given store slug.
     *
     * @param storeSlug the slug used to identify the store (e.g., "my-store")
     * @return a tenant identifier in the format {@code tenant_<storeId>}
     * @throws EntityNotFoundException if no store is found with the given slug
     */
    @Override
    public String resolveTenantFromSlug(String storeSlug) {
        Store store = storeRepository.findBySlug(storeSlug)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this slug: " + storeSlug));

        String storeId = store.getId().toString().replace("-", "_");
        return "tenant_" + storeId;
    }
}
