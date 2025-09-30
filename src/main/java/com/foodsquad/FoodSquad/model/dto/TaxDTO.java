package com.foodsquad.FoodSquad.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TaxDTO {

    private UUID id;

    private String name;
    private Double rate;
}
