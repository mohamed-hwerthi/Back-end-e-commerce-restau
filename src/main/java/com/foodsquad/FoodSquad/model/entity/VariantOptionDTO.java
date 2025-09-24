package com.foodsquad.FoodSquad.model.entity;


import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class   VariantOptionDTO {
    private UUID id;
    private String value;
    private BigDecimal price;
    private String sku  ;
    private String quantity;
    private UUID productVariantId ;
    private UUID variantAttributeID ;
}