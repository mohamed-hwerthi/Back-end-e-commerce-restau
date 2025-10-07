package com.foodsquad.FoodSquad.service.admin.dec;


import java.math.BigDecimal;

public interface PaymentMethodStrategy {
    boolean pay(BigDecimal amount);

    String getMethodName();
}