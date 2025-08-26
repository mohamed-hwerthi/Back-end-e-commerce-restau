package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.TaxDTO;
import com.foodsquad.FoodSquad.model.entity.Tax;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MediaMapper.class})
public interface TaxMapper {

    Tax toEntity(TaxDTO taxDTO);

    TaxDTO toDto(Tax tax);

}
