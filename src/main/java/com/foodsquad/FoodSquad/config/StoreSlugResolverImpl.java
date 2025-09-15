package com.foodsquad.FoodSquad.config;

import com.foodsquad.FoodSquad.model.entity.Store;
import com.foodsquad.FoodSquad.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreSlugResolverImpl implements StoreSlugResolver {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreSlugResolverImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public String resolveTenantFromSlug(String storeSlug) {
        Store store = storeRepository.findBySlug(storeSlug).orElseThrow(() -> new EntityNotFoundException("Store not  found with this  slug " + storeSlug));
        if (store == null) {
            return null;
        }
        String storeId = store.getId().toString().replace("-", "_");
        return "tenant_" + storeId;
    }
}
