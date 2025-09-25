package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.dto.SupplementGroupDTO;
import com.foodsquad.FoodSquad.model.entity.SupplementGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplementGroupMapper {

    @Mapping(target = "product", ignore = true)
    SupplementGroup toEntity(SupplementGroupDTO dto);

    SupplementGroupDTO toDto(SupplementGroup entity);

    List<SupplementGroup> toEntityList(List<SupplementGroupDTO> dtos);

    List<SupplementGroupDTO> toDtoList(List<SupplementGroup> entities);
}
