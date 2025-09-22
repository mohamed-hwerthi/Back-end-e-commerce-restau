package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeDTO {
    private UUID id;
    private String name;
    private Long productId;
    private List<ProductAttributeValue> values;
}
