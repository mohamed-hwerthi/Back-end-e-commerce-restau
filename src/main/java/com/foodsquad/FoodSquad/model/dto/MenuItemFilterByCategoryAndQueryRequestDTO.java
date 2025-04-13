package com.foodsquad.FoodSquad.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class MenuItemFilterByCategoryAndQueryRequestDTO {
    private String query ;
    private List<Long > categoriesIds ;

}
