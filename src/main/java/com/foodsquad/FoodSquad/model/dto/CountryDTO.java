package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CountryDTO {

    private String id;

    @NotBlank(message = "Country code cannot be blank")
    private String code;

    @NotEmpty(message = "Country name cannot be blank")
    private LocalizedString name;

    private String flagUrl;
}
