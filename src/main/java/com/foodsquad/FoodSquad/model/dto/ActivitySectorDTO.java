package com.foodsquad.FoodSquad.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ActivitySectorDTO {
    private UUID id;
    private String code;
    private String name;
}
