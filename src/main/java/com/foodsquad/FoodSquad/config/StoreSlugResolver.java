package com.foodsquad.FoodSquad.config;

public interface StoreSlugResolver {
    /**
     * Returns tenant identifier (schema) for given store slug.
     * Returns null if no mapping found.
     */
    String resolveTenantFromSlug(String storeSlug);
}
