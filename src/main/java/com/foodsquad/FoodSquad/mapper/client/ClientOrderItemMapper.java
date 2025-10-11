package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderItemDTO;
import com.foodsquad.FoodSquad.model.entity.Media;
import com.foodsquad.FoodSquad.model.entity.OrderItem;
import com.foodsquad.FoodSquad.model.entity.Product;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface ClientOrderItemMapper extends GenericMapper<OrderItem, ClientOrderItemDTO> {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", ignore = true)
    @Mapping(target = "totalPrice", expression = "java(entity.calculateItemTotal())")
    @Override
    ClientOrderItemDTO toDto(OrderItem entity);

    @Mapping(target = "product", ignore = true)
    @Override
    OrderItem toEntity(ClientOrderItemDTO dto);

    /**
     * Populates the product name with localized value based on LocaleContext.
     */
    @AfterMapping
    default void applyLocalizedProductName(OrderItem entity, @MappingTarget ClientOrderItemDTO dto) {
        String locale = LocaleContext.get();

        Optional.ofNullable(entity.getProduct())
                .map(Product::getTitle)
                .map(titleMap -> titleMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setProductName);
    }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleMediaUrls(OrderItem entity, @MappingTarget ClientOrderItemDTO dto) {
        if(!ObjectUtils.isEmpty(entity.getProduct().getMedias()) ){
            dto.setMediasUrls(entity.getProduct().getMedias().stream().map(Media::getUrl).toList());
        }


    }
}
