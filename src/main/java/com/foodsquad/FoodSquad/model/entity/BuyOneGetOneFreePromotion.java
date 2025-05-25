package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyOneGetOneFreePromotion extends Promotion {

    private int requiredQuantity = 1;

    private int freeQuantity = 1;


}
