package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductOption;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductOption;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientProductOptionMapper extends GenericMapper<ProductOption, ClientProductOption> {

    @Mapping(target = "inStock", ignore = true)
    @Override
    ClientProductOption toDto(ProductOption entity);

    @Override
    default ProductOption toEntity(ClientProductOption dto) {
        return null ;
    }


    @Override
    default void updateEntityFromDto(ClientProductOption dto, ProductOption entity) {

    }

}
