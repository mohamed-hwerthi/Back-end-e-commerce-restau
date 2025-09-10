package com.foodsquad.FoodSquad.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class StoreBasicDataDTO {
    private UUID storeId;
    private String storeSlug;
    private String storeName;
}
