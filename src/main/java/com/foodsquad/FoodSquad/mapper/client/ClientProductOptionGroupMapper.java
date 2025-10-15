package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductOptionGroup;
import com.foodsquad.FoodSquad.model.entity.ProductOptionGroup;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING , uses = {ClientProductOptionMapper.class})
public interface ClientProductOptionGroupMapper extends GenericMapper<ProductOptionGroup, ClientProductOptionGroup> {

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "options"  , source = "productOptions")
    ClientProductOptionGroup toDto(ProductOptionGroup entity);
    @Override
    default   ProductOptionGroup toEntity(ClientProductOptionGroup dto){
        return null  ;
    }

    @Override
   default  void updateEntityFromDto(ClientProductOptionGroup dto, ProductOptionGroup entity){
    }

    @AfterMapping
    default void applyTranslation(ProductOptionGroup entity, @MappingTarget ClientProductOptionGroup dto) {
        String locale = LocaleContext.get();

        mapName(entity, dto, locale);
    }

    private void mapName(ProductOptionGroup entity, ClientProductOptionGroup dto, String locale) {
        Optional.ofNullable(entity.getName())
                .map(nameMap -> nameMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setName);
    }

}
