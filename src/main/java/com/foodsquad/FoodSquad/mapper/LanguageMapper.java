package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.LanguageDTO;
import com.foodsquad.FoodSquad.model.dto.LanguageLocalizedDTO;
import com.foodsquad.FoodSquad.model.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {CountryMapper.class})
public interface LanguageMapper {

    @Mapping(target = "name", ignore = true)
    LanguageDTO toDtoWithoutLocale(Language entity);

    @Mapping(target = "name", ignore = true)
    Language toEntityWithoutLocale(LanguageDTO dto);

    LanguageLocalizedDTO toLocalizedDto(Language entity);

    Language toEntity(LanguageLocalizedDTO dto);

    default LanguageDTO toDto(Language entity, LocalizedStringMapper localizedStringMapper) {
        if (entity == null) return null;
        LanguageDTO dto = toDtoWithoutLocale(entity);
        dto.setName(localizedStringMapper.toString(entity.getName()));
        return dto;
    }

    default Language toEntity(LanguageDTO dto, LocalizedStringMapper localizedStringMapper) {
        if (dto == null) return null;
        Language country = toEntityWithoutLocale(dto);
        country.setName(localizedStringMapper.toLocalized(dto.getName()));
        return country;
    }
}
