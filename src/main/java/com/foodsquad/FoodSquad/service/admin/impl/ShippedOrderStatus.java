package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.service.admin.dec.OrderStatus;

public class ShippedOrderStatus implements OrderStatus {

    @Override
    public OrderStatus nextStatus() {
        return new DeliveredOrderStatus();
    }

    @Override
    public OrderStatus prevStatus() {
        return new ProcessingOrderStatus();
    }

    @Override
    public String getStatusName() {
        return "Shipped";
    }
}
