package com.foodsquad.FoodSquad.service.admin.dec;


public interface OrderStatus {
    OrderStatus nextStatus();

    OrderStatus prevStatus();

    String getStatusName();
}

