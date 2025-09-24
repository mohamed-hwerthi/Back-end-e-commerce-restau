package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.VariantOptionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class VariantDTO {
    private UUID id  ;
    private LocalizedString  name  ;
    private List<VariantOptionDTO> options  = new ArrayList<>() ;

}
