package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.CustomAttributeType;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.validation.annotations.NotEmptyLocalizedString;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CustomAttributeDTO {

    private UUID id;
    @NotEmptyLocalizedString(message = "custom attribute must have at least one translated field  ")
    private LocalizedString name;
    @NotNull(message = "custom attribute value cannot be null")
    private String value;
    @NotEmpty(message = "custom attribute type cannot be null")
    private CustomAttributeType type;
}
