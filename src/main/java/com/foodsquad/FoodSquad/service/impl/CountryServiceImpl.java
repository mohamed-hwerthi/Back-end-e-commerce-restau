package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CountryMapper;
import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import com.foodsquad.FoodSquad.repository.CountryRepository;
import com.foodsquad.FoodSquad.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    @Transactional
    public Country save(CountryDTO countryDTO) {
        logger.info("Saving new country: {}", countryDTO.getName());
        if (countryRepository.existsByCode(countryDTO.getCode())) {
            throw new IllegalArgumentException("Country with code " + countryDTO.getCode() + " already exists");
        }
        if (countryRepository.existsByName(countryDTO.getName())) {
            throw new IllegalArgumentException("Country with name " + countryDTO.getName() + " already exists");
        }
        // Update fields

        Country country = countryMapper.toEntity(countryDTO);
        return countryRepository.save(country);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> findAll() {
        logger.info("Fetching all countries");
        return countryRepository.findAll();
    }

    @Override
    @Transactional
    public Country update(String code, CountryDTO countryDTO) {
        logger.info("Updating country with code: {}", code);
        Country existingCountry = countryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with code: " + code));

        if (!existingCountry.getName().equals(countryDTO.getName()) &&
                countryRepository.existsByName(countryDTO.getName())) {
            throw new IllegalArgumentException("Country with name " + countryDTO.getName() + " already exists");
        }

        existingCountry.setName(countryDTO.getName());
        existingCountry.setFlagUrl(countryDTO.getFlagUrl());

        return countryRepository.save(existingCountry);
    }

    @Override
    @Transactional
    public void delete(String id) {
        logger.info("Deleting country with code: {}", id);
        if (!countryRepository.existsByCode(id)) {
            throw new IllegalArgumentException("Country not found with id: " + id);
        }
        countryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Country findByCode(String code) {
        logger.info("Fetching country with code: {}", code);
        return countryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Country not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return countryRepository.existsByCode(code);
    }
}
