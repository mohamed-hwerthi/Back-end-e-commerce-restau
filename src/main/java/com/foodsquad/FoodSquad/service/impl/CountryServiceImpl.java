package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CountryMapper;
import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import com.foodsquad.FoodSquad.repository.CountryRepository;
import com.foodsquad.FoodSquad.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {


    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;


    @Override
    @Transactional(readOnly = true)
    public List<CountryDTO> findAll() {
        log.info("Fetching all countries");
        List<Country> countries = countryRepository.findAll();

        List<CountryDTO> dtos = countries.stream()
                .map(countryMapper::toDto)
                .collect(Collectors.toList());

        log.info("Found {} countries", dtos.size());
        return dtos;
    }


}
