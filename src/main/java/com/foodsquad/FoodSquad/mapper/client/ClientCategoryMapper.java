package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientCategoryDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Media;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientCategoryMapper extends GenericMapper<Category, ClientCategoryDTO> {


    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "mediasUrls", ignore = true)
    @Override
    ClientCategoryDTO toDto(Category entity);

    @Override
    default Category toEntity(ClientCategoryDTO dto) {
        return null;
    }

    @Override
    default void updateEntityFromDto(ClientCategoryDTO dto, Category entity) {

    }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleTranslation(Category entity, @MappingTarget ClientCategoryDTO dto) {
        String locale = LocaleContext.get();

        Optional.ofNullable(entity.getName())
                .map(titleMap -> titleMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setName);
        Optional.ofNullable(entity.getDescription())
                .map(descriptionMap -> descriptionMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setDescription);

    }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleMediaUrls(Category entity, @MappingTarget ClientCategoryDTO dto) {
        if(!ObjectUtils.isEmpty(entity.getMedias())){
            dto.setMediasUrls(entity.getMedias().stream().map(Media::getUrl).toList());
        }


    }


}
