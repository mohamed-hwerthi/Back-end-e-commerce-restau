package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeDTO {
    private UUID id;
    private LocalizedString name;

}
