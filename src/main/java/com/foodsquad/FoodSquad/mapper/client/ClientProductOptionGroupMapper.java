package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductOption;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductOptionGroup;
import com.foodsquad.FoodSquad.model.entity.ProductOption;
import com.foodsquad.FoodSquad.model.entity.ProductOptionGroup;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING , uses = {ClientProductOptionMapper.class})
public interface ClientProductOptionGroupMapper extends GenericMapper<ProductOptionGroup, ClientProductOptionGroup> {

    @Mapping(target = "name", ignore = true)
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
        dto.setOptions(mapOptions(entity.getProductOptions(), locale));
    }

    private void mapName(ProductOptionGroup entity, ClientProductOptionGroup dto, String locale) {
        Optional.ofNullable(entity.getName())
                .map(nameMap -> nameMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setName);
    }

    default List<ClientProductOption> mapOptions(List<ProductOption> options, String locale) {
        if (ObjectUtils.isEmpty(options)) return List.of();

        return options.stream().map(option -> {
            ClientProductOption clientOption = new ClientProductOption();
            if (option.getLinkedProduct() != null) {
                clientOption.setOptionId(option.getLinkedProduct().getId());
            }
            return clientOption;
        }).toList();
    }

}
