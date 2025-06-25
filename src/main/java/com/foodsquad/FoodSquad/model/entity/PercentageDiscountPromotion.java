package com.foodsquad.FoodSquad.model.entity;

import com.foodsquad.FoodSquad.model.dto.DiscountType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PercentageDiscountPromotion extends Promotion {

        private Integer discountPercentage;

        private DiscountType discountType ;

        private  Double   promotionalPrice  ;

}