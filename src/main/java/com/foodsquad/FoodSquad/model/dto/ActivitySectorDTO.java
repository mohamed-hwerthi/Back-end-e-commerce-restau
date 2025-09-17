package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ActivitySectorDTO {
    private UUID id;
    private String code;
    private LocalizedString name;
}
