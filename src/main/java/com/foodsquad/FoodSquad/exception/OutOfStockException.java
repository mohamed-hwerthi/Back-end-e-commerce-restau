package com.foodsquad.FoodSquad.exception;


import lombok.Data;

@Data
public class OutOfStockException extends RuntimeException {

    private String ProductName;

    public OutOfStockException(String ProductName) {
        super(" is out of stock");

    }

}
