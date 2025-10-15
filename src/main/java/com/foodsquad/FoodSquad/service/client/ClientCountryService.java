package com.foodsquad.FoodSquad.service.client;

import com.foodsquad.FoodSquad.model.dto.client.ClientCountryDTO;

import java.util.List;

public interface ClientCountryService {
    List<ClientCountryDTO> getAllCountries();
}
