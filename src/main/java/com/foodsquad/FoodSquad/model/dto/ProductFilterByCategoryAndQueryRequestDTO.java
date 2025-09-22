package com.foodsquad.FoodSquad.model.dto;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductFilterByCategoryAndQueryRequestDTO {
    private String query;
    private List<UUID> categoriesIds;
    private Boolean inStock;

}
