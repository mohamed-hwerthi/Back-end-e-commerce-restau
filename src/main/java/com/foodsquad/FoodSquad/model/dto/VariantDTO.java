package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.VariantOptionDTO;
import com.foodsquad.FoodSquad.validation.annotations.NotEmptyLocalizedString;
import jakarta.validation.Valid;
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

    @NotEmptyLocalizedString(message = "variant attribute name must have at leat one translated  field ")
    private LocalizedString attributeName;

    @NotEmpty(message = "One Variant mut have at leat one Option")
    @Valid
    private List<VariantOptionDTO> options = new ArrayList<>();

}
