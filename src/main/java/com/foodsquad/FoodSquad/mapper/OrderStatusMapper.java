package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.OrderStatusDTO;
import com.foodsquad.FoodSquad.model.entity.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING
)
public interface OrderStatusMapper extends GenericMapper<OrderStatus, OrderStatusDTO> {
}
