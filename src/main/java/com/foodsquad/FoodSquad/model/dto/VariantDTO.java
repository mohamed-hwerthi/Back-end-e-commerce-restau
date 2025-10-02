package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.VariantOptionDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class VariantDTO {
    private UUID attributeId;
    @NotEmpty(message = "variant attribute name cannot not  be null")
    private LocalizedString attributeName;
    @NotEmpty(message = "variant  options  cannot be empty ")
    private List<VariantOptionDTO> options = new ArrayList<>();

}
