package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PercentageDiscountPromotion extends Promotion {

        private Integer discountPercentage;

}