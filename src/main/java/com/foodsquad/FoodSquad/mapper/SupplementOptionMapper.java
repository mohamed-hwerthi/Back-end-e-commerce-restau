package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.dto.SupplementOptionDTO;
import com.foodsquad.FoodSquad.model.entity.SupplementOption;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplementOptionMapper {

    SupplementOption toEntity(SupplementOptionDTO dto);

    SupplementOptionDTO toDto(SupplementOption entity);

    List<SupplementOption> toEntityList(List<SupplementOptionDTO> dtos);

    List<SupplementOptionDTO> toDtoList(List<SupplementOption> entities);
}
