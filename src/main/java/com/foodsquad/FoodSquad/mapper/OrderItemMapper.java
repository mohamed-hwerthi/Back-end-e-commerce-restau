package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.OrderItemDTO;
import com.foodsquad.FoodSquad.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper extends GenericMapper<OrderItem, OrderItemDTO> {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.title")
    @Override
    OrderItemDTO toDto(OrderItem entity);

}
