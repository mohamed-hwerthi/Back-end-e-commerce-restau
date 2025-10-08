package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.OrderStatusDTO;
import com.foodsquad.FoodSquad.model.entity.OrderStatus;
import org.mapstruct.Mapper;

@Mapper
public interface OrderStatusMapper extends GenericMapper<OrderStatus, OrderStatusDTO> {
}
