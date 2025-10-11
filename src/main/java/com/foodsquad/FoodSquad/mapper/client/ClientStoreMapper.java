package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientStoreDTO;
import com.foodsquad.FoodSquad.model.entity.Store;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientStoreMapper extends GenericMapper<Store, ClientStoreDTO> {
    @Mapping(target = "countryName", ignore = true)
    @Mapping(target = "about", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "email", source = "owner.email")
    @Mapping(target = "phoneNumber", source = "owner.phoneNumber")
    ClientStoreDTO toDto(Store store);


    @Override
    default  Store toEntity(ClientStoreDTO store){
        return null ;
    }

    @Override
    default void updateEntityFromDto(ClientStoreDTO dto, Store entity) {

    }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleTranslation(Store entity, @MappingTarget ClientStoreDTO dto) {
        String locale = LocaleContext.get();

        Optional.ofNullable(entity.getAbout())
                .map(aboutMap -> aboutMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setAbout);

    }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleMediaUrls(Store entity, @MappingTarget ClientStoreDTO dto) {
        if (!ObjectUtils.isEmpty(entity.getLogo())) {
            dto.setLogo(entity.getLogo().getUrl());
        }


    }

}
