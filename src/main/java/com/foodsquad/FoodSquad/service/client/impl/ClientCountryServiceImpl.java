package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.mapper.client.ClientCountryMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientCountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import com.foodsquad.FoodSquad.repository.CountryRepository;
import com.foodsquad.FoodSquad.service.client.ClientCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientCountryServiceImpl implements ClientCountryService {

    private final CountryRepository countryRepository;
    private final ClientCountryMapper clientCountryMapper;

    @Autowired
    public ClientCountryServiceImpl(CountryRepository countryRepository, 
                                   ClientCountryMapper clientCountryMapper) {
        this.countryRepository = countryRepository;
        this.clientCountryMapper = clientCountryMapper;
    }

    @Override
    public List<ClientCountryDTO> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(clientCountryMapper::toDto)
                .collect(Collectors.toList());
    }
}
