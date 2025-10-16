package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel =  MappingConstants.ComponentModel.SPRING  , uses = {CustomerMapper.class ,  OrderStatusMapper.class , AddressMapper.class , OrderItemMapper.class})
public  interface  OrderMapper  extends  GenericMapper<Order, OrderDTO> {


    @Override
    default void updateEntityFromDto(OrderDTO dto, Order entity) {

    }
}

