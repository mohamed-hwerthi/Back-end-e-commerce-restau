package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageLocalizedDTO {
    private String code;
    private LocalizedString name;
}
