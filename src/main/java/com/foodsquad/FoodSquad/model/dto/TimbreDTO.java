package com.foodsquad.FoodSquad.model.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TimbreDTO {

    private UUID id;

    private BigDecimal amount;

}
