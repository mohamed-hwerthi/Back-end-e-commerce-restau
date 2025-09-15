package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.CountryMapper;
import com.foodsquad.FoodSquad.mapper.LocalizedStringMapper;
import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.dto.CountryLocalizedDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import com.foodsquad.FoodSquad.repository.CountryRepository;
import com.foodsquad.FoodSquad.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final LocalizedStringMapper localizedStringMapper;
    private final LocaleContext localeContext;

    @Override
    @Transactional
    public CountryDTO save(CountryLocalizedDTO countryDTO) {
        logger.info("Saving new country with code: {}, name: {}", countryDTO.getCode(), countryDTO.getName());

        if (countryRepository.existsByCode(countryDTO.getCode())) {
            String msg = "Country with code " + countryDTO.getCode() + " already exists";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }

        Country country = countryMapper.toEntity(countryDTO);
        Country savedCountry = countryRepository.save(country);

        logger.info("Country saved successfully with code: {}", savedCountry.getCode());
        return countryMapper.toDto(country , localizedStringMapper) ;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDTO> findAll() {
        logger.info("Fetching all countries");
        List<Country> countries = countryRepository.findAll();

        List<CountryDTO> dtos = countries.stream()
                .map(country -> countryMapper.toDto(country , localizedStringMapper))
                .collect(Collectors.toList());

        logger.info("Found {} countries", dtos.size());
        return dtos;
    }

    @Override
    @Transactional
    public CountryDTO update(String code, CountryDTO countryDTO) {
        logger.info("Updating country with code: {}", code);

        Country existingCountry = countryRepository.findByCode(code)
                .orElseThrow(() -> {
                    String msg = "Country not found with code: " + code;
                    logger.error(msg);
                    return new IllegalArgumentException(msg);
                });

        String currentLocale = localeContext.getLocale();
        existingCountry.getName().getTranslations().put(currentLocale, countryDTO.getName());

        existingCountry.setFlagUrl(countryDTO.getFlagUrl());

        Country updatedCountry = countryRepository.save(existingCountry);
        logger.info("Country updated successfully with code: {}", updatedCountry.getCode());
        return  countryMapper.toDto(updatedCountry , localizedStringMapper) ;
    }

    @Override
    @Transactional
    public void delete(String code) {
        logger.info("Deleting country with code: {}", code);

        if (!countryRepository.existsByCode(code)) {
            String msg = "Country not found with code: " + code;
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }

        countryRepository.deleteById(code);
        logger.info("Country deleted successfully with code: {}", code);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDTO findByCode(String code) {
        logger.info("Fetching country with code: {}", code);
        Country country = countryRepository.findByCode(code)
                .orElseThrow(() -> {
                    String msg = "Country not found with code: " + code;
                    logger.error(msg);
                    return new IllegalArgumentException(msg);
                });

        String currentLocale = localeContext.getLocale();
        CountryDTO dto = countryMapper.toDto(country , localizedStringMapper);
        logger.info("Country fetched successfully with code: {}", code);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        boolean exists = countryRepository.existsByCode(code);
        logger.info("Country with code {} exists: {}", code, exists);
        return exists;
    }
}
