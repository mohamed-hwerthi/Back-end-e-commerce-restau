package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientStoreDTO;
import com.foodsquad.FoodSquad.model.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ActivitySectorMapper.class, CurrencyMapper.class, LanguageMapper.class})
public interface StoreMapper {

    @Mapping(target = "email", source = "owner.email")
    @Mapping(target = "phoneNumber", source = "owner.phoneNumber")
    @Mapping(target = "password", ignore = true)
    StoreDTO toDto(Store store);

    Store toEntity(StoreDTO storeDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activitySector", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "defaultLanguage", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateStoreFields(StoreDTO dto, @MappingTarget Store entity);


}
