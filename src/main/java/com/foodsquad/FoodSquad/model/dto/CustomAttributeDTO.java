package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.CustomAttributeType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomAttributeDTO {
    private UUID id;
    private String name;
    private String value;
    private CustomAttributeType type;
}
