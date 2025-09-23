package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.RequestVariantOptionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class RequestVariantDTO {
     private UUID id  ;
     private LocalizedString  name  ;
     private List<RequestVariantOptionDTO> options  = new ArrayList<>() ;

}
