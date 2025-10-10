package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;

import java.util.List;

public interface CountryService {

    List<CountryDTO> findAll();

    Country findById(String id);


}