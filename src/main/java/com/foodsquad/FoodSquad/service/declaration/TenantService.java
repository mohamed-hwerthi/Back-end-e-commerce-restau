package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.entity.User;

public interface TenantService {

    void createTenant(String storeId, User user);

}
