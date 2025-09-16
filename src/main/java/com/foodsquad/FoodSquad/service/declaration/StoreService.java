package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.dto.LanguageDTO;
import com.foodsquad.FoodSquad.model.dto.StoreBasicDataDTO;
import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreDTO save(StoreDTO storeDTO);

    StoreDTO update(UUID storeId, StoreDTO storeDTO);

    List<StoreDTO> findAll();

    StoreDTO findById(UUID id);

    void delete(UUID id);

    StoreDTO findByOwner(User owner);

    StoreBasicDataDTO findByEmail();

    StoreBasicDataDTO findByStoreSlug(String SToreSlug);

    CurrencyDTO findCurrencyOfStore(UUID storeId);

    LanguageDTO findDefaultLanguageOfTheStore(UUID storeId) ;


}
