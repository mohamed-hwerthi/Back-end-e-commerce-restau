package com.foodsquad.FoodSquad.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SupplementGroupDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotEmpty(message = "Group name cannot be blank")
    private LocalizedString name;

    private boolean obligatory;

    private List<SupplementOptionDTO> supplementOptions = new ArrayList<>();
}
