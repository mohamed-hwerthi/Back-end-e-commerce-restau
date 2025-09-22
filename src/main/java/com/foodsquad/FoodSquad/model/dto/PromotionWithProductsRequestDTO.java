package com.foodsquad.FoodSquad.model.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PromotionWithProductsRequestDTO {
    private PromotionDTO promotion;
    private List<UUID> ProductsIds = new ArrayList<>();
}
