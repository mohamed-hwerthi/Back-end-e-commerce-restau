package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;

import java.util.List;

public interface CountryService {
    Country save(CountryDTO countryDTO);

    List<Country> findAll();

    Country update(String code, CountryDTO countryDTO);

    void delete(String id);

    Country findByCode(String code);

    boolean existsByCode(String code);
}
