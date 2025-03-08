package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.model.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    Order toEntity(OrderDTO orderDTO);

    OrderDTO toDto(Order order);

}
