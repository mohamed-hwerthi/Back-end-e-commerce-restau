package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyMapper {
    Currency toEntity(CurrencyDTO currencyDto);

    CurrencyDTO toDto(Currency currency);

}
