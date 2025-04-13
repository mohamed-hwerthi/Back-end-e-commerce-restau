package com.foodsquad.FoodSquad.model.dto;

import lombok.Data;

@Data
public class TaxDTO {

    private Long id;

    private String name; // e.g., Tva, Service Tax
    private Double rate; // e.g., 7.5%
}
