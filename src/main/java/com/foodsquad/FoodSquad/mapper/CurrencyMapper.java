package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyMapper {
    Currency toEntity(CurrencyDTO currencyDto);

    CurrencyDTO toDto(Currency currency);

}
