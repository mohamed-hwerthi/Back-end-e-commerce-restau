package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.model.dto.OrderItemDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING , uses = {CustomerMapper.class, OrderStatusMapper.class , OrderItemMapper.class})
public abstract class OrderMapper {

    @Autowired
    protected LocaleContext localeContexte;


    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.title", target = "productName")
    @Mapping(target = "totalPrice", expression = "java(orderItem.calculateItemTotal())")
    public abstract OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    protected String map(LocalizedString value) {
        if (value == null) return null;
        return value.get(localeContexte.getLocale());
    }
}

