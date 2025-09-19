package com.foodsquad.FoodSquad.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TimbreDTO {

    private UUID id;

    private BigDecimal amount;

}
