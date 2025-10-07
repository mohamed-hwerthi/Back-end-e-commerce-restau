package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.service.admin.dec.OrderStatus;

public class DeliveredOrderStatus implements OrderStatus {

    @Override
    public OrderStatus nextStatus() {
        return this;
    }

    @Override
    public OrderStatus prevStatus() {
        return new ShippedOrderStatus();
    }

    @Override
    public String getStatusName() {
        return "Delivered";
    }
}
