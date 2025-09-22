package com.foodsquad.FoodSquad.model.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantOptionDTO {
    @NotBlank(message = "attribute has to be not null ")
    private String attributeName;
    @NotBlank(message = "value has to be not null ")
    private String value;
}