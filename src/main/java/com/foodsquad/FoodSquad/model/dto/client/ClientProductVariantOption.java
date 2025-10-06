package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientProductVariantOption {
    private UUID variantId;
    private String variantValue;
    private BigDecimal variantPrice;
    private boolean inStock;
}