package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientCountryDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import com.foodsquad.FoodSquad.model.entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ClientCountryMapper extends GenericMapper<Country, ClientCountryDTO> {
    ClientCountryMapper INSTANCE = Mappers.getMapper(ClientCountryMapper.class);
    @Mapping(target = "name"  , ignore = true)
    @Override
    ClientCountryDTO toDto(Country country);

    @Override
    default Country toEntity(ClientCountryDTO dto) {
        return null;
    }

    @Override
    default void updateEntityFromDto(ClientCountryDTO dto, Country entity) {

    }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleTranslation(Country entity, @MappingTarget ClientCountryDTO dto) {
        String locale = LocaleContext.get();

        Optional.ofNullable(entity.getName())
                .map(titleMap -> titleMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setName);


    }
    

}
