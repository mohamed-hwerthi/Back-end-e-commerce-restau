package com.foodsquad.FoodSquad.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CurrencyDTO {

    private UUID d;
    private String name;
    private String symbol;
    private int scale;

}
