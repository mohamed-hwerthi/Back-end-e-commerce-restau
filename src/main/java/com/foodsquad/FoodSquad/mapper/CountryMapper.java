package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.dto.CountryLocalizedDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocalizedStringMapper.class)
public interface CountryMapper {

    @Mapping(target = "name", ignore = true)
    CountryDTO toDtoWithoutLocale(Country entity);

    @Mapping(target = "name", ignore = true)
    Country toEntityWithoutLocale(CountryDTO dto);

    CountryLocalizedDTO toLocalizedDto(Country entity);

    Country toEntity(CountryLocalizedDTO dto);

    default CountryDTO toDto(Country entity, LocalizedStringMapper localizedStringMapper) {
        if (entity == null) return null;
        CountryDTO dto = toDtoWithoutLocale(entity);
        dto.setName(localizedStringMapper.toString(entity.getName()));
        return dto;
    }

    default Country toEntity(CountryDTO dto, LocalizedStringMapper localizedStringMapper) {
        if (dto == null) return null;
        Country country = toEntityWithoutLocale(dto);
        country.setName(localizedStringMapper.toLocalized(dto.getName()));
        return country;
    }
}

