package com.foodsquad.FoodSquad.model.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PromotionWithMenuItemsRequestDTO {
    private PromotionDTO promotion;
    private List<UUID> menuItemsIds = new ArrayList<>();
}
