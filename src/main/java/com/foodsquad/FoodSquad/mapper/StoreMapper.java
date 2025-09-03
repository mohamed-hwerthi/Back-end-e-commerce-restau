package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.model.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring" , uses = {ActivitySectorMapper.class})
public interface StoreMapper {

    StoreDTO toDto(Store store);

    Store toEntity(StoreDTO storeDTO);
}
