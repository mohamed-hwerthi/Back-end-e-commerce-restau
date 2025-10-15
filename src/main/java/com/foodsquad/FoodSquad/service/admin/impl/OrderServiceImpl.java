package com.foodsquad.FoodSquad.service.admin.impl;


import com.foodsquad.FoodSquad.mapper.OrderMapper;
import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.service.admin.dec.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDTO> searchOrders(String statusCode, LocalDateTime startDate, LocalDateTime endDate) {
        var orders = orderRepository.findOrdersByFilters(statusCode, startDate, endDate);
        return orderMapper.toDtoList(orders);
    }
}