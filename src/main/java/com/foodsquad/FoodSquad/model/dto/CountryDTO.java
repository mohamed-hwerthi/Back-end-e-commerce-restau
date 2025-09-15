package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CountryDTO {

    private UUID id;

    @NotBlank(message = "Country code cannot be blank")
    private String code;

    @NotBlank(message = "Country name cannot be blank")
    private String name;

    private String flagUrl;
}
