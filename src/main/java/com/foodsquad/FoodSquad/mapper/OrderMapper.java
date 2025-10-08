package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.model.dto.OrderItemDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrderStatusMapper.class})
public abstract class OrderMapper {

    @Autowired
    protected LocaleContext localeContexte;

    @Mapping(source = "customer.id", target = "customer.id")
    @Mapping(source = "customer.firstName", target = "customer.firstName")
    @Mapping(source = "customer.lastName", target = "customer.lastName")
    @Mapping(source = "customer.email", target = "customer.email")
    @Mapping(source = "customer.phone", target = "customer.phone")
    @Mapping(source = "status.name", target = "status")
    public abstract ClientOrderDTO toClientOrderDTO(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.title", target = "productName")
    @Mapping(target = "totalPrice", expression = "java(orderItem.calculateItemTotal())")
    public abstract OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    protected String map(LocalizedString value) {
        if (value == null) return null;
        return value.get(localeContexte.getLocale());
    }
}

