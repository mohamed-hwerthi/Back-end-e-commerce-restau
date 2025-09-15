package com.foodsquad.FoodSquad.exception;


import lombok.Data;

@Data
public class OutOfStockException extends RuntimeException {

    private String menuItemName;

    public OutOfStockException(String menuItemName) {
        super(" is out of stock");

    }

}
