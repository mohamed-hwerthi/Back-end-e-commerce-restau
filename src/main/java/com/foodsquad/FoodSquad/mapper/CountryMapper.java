package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.dto.CountryLocalizedDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Context;

@Mapper(componentModel = "spring", uses = LocalizedStringMapper.class)
public interface CountryMapper {

    @Mapping(target = "name", ignore = true)
    CountryDTO toDto(Country entity);

    @Mapping(target = "name", ignore = true)
    Country toEntity(CountryDTO dto);

    CountryLocalizedDTO toLocalizedDto(Country entity);

    Country toEntity(CountryLocalizedDTO dto);

    default CountryDTO toDtoWithLocale(Country entity, @Context String locale, LocalizedStringMapper mapper) {
        if (entity == null) return null;
        CountryDTO dto = toDto(entity);
        dto.setName(mapper.toString(entity.getName(), locale));
        return dto;
    }

    default Country toEntityWithLocale(CountryDTO dto, @Context String locale, LocalizedStringMapper mapper) {
        if (dto == null) return null;
        Country country = toEntity(dto);
        country.setName(mapper.toLocalized(dto.getName(), locale));
        return country;
    }
}
