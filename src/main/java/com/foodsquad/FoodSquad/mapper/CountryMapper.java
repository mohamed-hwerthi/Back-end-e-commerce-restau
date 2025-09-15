package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {


    Country toEntity(CountryDTO countryDTO);

    CountryDTO toDto(Country country);
}
