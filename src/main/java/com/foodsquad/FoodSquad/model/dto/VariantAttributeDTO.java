package com.foodsquad.FoodSquad.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VariantAttributeDTO {
    private UUID id;
    private String name;
    private String value;
}
