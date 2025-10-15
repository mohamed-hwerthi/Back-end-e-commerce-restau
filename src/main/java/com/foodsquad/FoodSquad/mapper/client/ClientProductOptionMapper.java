package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductOptionDTO;
import com.foodsquad.FoodSquad.model.entity.ProductOption;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientProductOptionMapper extends GenericMapper<ProductOption, ClientProductOptionDTO> {

    @Mapping(target = "inStock", ignore = true)
    @Mapping(target = "optionPrice", source = "overridePrice")
    @Mapping(target = "optionId"  , source = "id")
    @Override
    ClientProductOptionDTO toDto(ProductOption entity);

    @Override
    default ProductOption toEntity(ClientProductOptionDTO dto) {
        return null;
    }
    @Override
    default void updateEntityFromDto(ClientProductOptionDTO dto, ProductOption entity) {
    }
    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleTranslation(ProductOption entity, @MappingTarget ClientProductOptionDTO dto) {
        String locale = LocaleContext.get();
        Optional.ofNullable(entity.getLinkedProduct().getTitle())
                .map(titleMap -> titleMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setOptionName);

    }

}
