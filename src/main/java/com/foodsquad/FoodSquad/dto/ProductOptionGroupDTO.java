package com.foodsquad.FoodSquad.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.validation.annotations.NotEmptyLocalizedString;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductOptionGroupDTO {

    private UUID id;

    @NotEmptyLocalizedString(message = "Product option group name  must have at least one translated field ")
    private LocalizedString name;
    @NotEmpty(message = "Product option group must have at leat one option ")
    private List<ProductOptionDTO> productOptions = new ArrayList<>();
}
