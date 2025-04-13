package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MediaMapper.class})
public interface CurrencyMapper {
    @Mapping(target = "menuItem", ignore = true)
    Currency toEntity(CurrencyDTO currencyDto);

    CurrencyDTO toDto(Currency currency);

    List<CurrencyDTO> toDTOList(List<Currency> currencies);
}
