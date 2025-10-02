package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.entity.User;

public interface TenantService {

    void createTenant(String storeId, User user);

}
