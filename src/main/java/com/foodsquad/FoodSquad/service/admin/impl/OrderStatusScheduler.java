package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusScheduler {

    @Autowired
    private OrderRepository orderRepository;

}
