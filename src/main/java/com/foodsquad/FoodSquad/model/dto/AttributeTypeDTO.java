package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttributeTypeDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    
    private String name;
    private String description;
    private String dataType; // e.g., "TEXT", "NUMBER", "BOOLEAN"
    private boolean required;
    private String validationRegex;
}
