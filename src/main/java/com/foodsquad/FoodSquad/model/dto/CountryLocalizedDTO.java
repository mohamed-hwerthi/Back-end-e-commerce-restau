package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryLocalizedDTO {

    private String code;
    private LocalizedString name;
    private String flagUrl;
}
