package com.foodsquad.FoodSquad.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RequestVariantOptionDTO {
    private UUID id;
    private String value;
    private BigDecimal price;
    private String quantity;
    private String sku;
}