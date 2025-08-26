package com.foodsquad.FoodSquad.exception;

public class ProductWithBarCodeExistException extends  RuntimeException{
    public ProductWithBarCodeExistException(String barCode){
        super("Product with bar code"+barCode+"exist ");

    }
}
