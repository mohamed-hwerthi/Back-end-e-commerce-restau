package com.foodsquad.FoodSquad.model.entity;

/*
 todo  : we have to change this enum to a class that contians data  :
 */


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public enum MenuItemCategory {
    PIZZA,
    BURGER,
    PASTA,
    SUSHI,
    SALAD,
    TACOS,
    DESSERT,
    OTHER
}



