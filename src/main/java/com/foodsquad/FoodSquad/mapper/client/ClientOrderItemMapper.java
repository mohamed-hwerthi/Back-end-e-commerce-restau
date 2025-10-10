package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderItemDTO;
import com.foodsquad.FoodSquad.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientOrderItemMapper  extends GenericMapper <OrderItem, ClientOrderItemDTO> {


    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.title")
    @Mapping(target = "totalPrice", expression = "java(entity.calculateItemTotal())")
    @Override
    ClientOrderItemDTO toDto(OrderItem entity);

    @Mapping(target = "product", ignore = true)
    @Override
    OrderItem toEntity(ClientOrderItemDTO dto);
}
