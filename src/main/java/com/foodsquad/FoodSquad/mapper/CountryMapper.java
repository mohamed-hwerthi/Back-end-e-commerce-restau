package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LocalizedStringMapper.class)
public interface CountryMapper {

    CountryDTO toDto(Country entity);

    Country toEntity(CountryDTO dto);


}

