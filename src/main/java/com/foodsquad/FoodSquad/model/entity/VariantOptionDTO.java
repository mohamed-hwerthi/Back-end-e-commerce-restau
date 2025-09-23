package com.foodsquad.FoodSquad.model.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VariantOptionDTO {
    private UUID id ;
    @NotBlank(message = "attribute has to be not null ")
    private LocalizedString attributeName;
    @NotBlank(message = "value has to be not null ")
    private String value;
}