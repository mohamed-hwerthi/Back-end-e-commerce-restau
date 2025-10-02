package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.CustomAttributeType;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomAttributeDTO {

    private UUID id;
    @NotEmpty(message = "custom attribute cannot  not be null ")
    private LocalizedString name;
    @NotNull(message = "custom attribute value cannot be null")
    private String value;
    @NotEmpty(message = "custom attribute type cannot be null")
    private CustomAttributeType type;
}
