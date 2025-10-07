package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.service.admin.dec.OrderStatus;

public class ProcessingOrderStatus implements OrderStatus {

    @Override
    public OrderStatus nextStatus() {
        return new ShippedOrderStatus();
    }

    @Override
    public OrderStatus prevStatus() {
        return this;
    }

    @Override
    public String getStatusName() {
        return "Processing";
    }
}
