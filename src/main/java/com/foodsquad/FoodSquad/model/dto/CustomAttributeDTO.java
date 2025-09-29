package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.CustomAttributeType;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomAttributeDTO {
    private UUID id;
    private LocalizedString name;
    private String value;
    private CustomAttributeType type;
}
