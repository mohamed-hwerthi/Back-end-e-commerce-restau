package com.foodsquad.FoodSquad.model.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PromotionWithMenuItemsRequestDTO {
private  PromotionDTO promotion  ;
private List<Long> menuItemsIds = new ArrayList<>()  ;
}
