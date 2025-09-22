package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
 public class ProductAttributeValueDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "Attribute type ID cannot be null")
    private UUID attributeTypeId;

    @NotBlank(message = "Value cannot be blank")
    private String value;

    private String displayName;

    private String additionalData;

    private AttributeTypeDTO attributeType;
}
