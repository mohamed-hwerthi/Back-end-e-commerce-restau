package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.model.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ActivitySectorMapper.class, CurrencyMapper.class, LanguageMapper.class})
public interface StoreMapper {

    @Mapping(target = "email", source = "owner.email")
    @Mapping(target = "phoneNumber", source = "owner.phoneNumber")
    @Mapping(target = "password", ignore = true)
    StoreDTO toDto(Store store);

    Store toEntity(StoreDTO storeDTO);
}
