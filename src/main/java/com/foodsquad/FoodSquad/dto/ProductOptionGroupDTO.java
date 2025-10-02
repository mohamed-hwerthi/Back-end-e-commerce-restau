package com.foodsquad.FoodSquad.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
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

    @NotEmpty(message = "Group name cannot be blank")
    private LocalizedString name;
    @NotEmpty(message = "Group must have at leat one option ")
    private List<ProductOptionDTO> productOptions = new ArrayList<>();
}
