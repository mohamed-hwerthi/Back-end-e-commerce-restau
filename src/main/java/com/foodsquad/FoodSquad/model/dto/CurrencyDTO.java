package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CurrencyDTO {

    private Long id;
    @NotBlank(message = "name  cannot be blank")
    private String name;
    private String symbol;
    private int scale;

}
