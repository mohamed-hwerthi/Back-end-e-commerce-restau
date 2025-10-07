package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientOptionDTO {
    private UUID optionId;
    private String name;
    private BigDecimal price;
}