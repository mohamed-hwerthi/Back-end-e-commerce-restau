package com.foodsquad.FoodSquad.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {
    private String code;
    private String name;
    private Country country;
}
