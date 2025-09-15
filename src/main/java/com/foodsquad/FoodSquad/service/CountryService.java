package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.dto.CountryLocalizedDTO;

import java.util.List;

public interface CountryService {
    CountryDTO save(CountryLocalizedDTO countryDTO);

    List<CountryDTO> findAll();

    CountryDTO update(String code, CountryDTO countryDTO);

    void delete(String id);

    CountryDTO findByCode(String code);

    boolean existsByCode(String code);
}
