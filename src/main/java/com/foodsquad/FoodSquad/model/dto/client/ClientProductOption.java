package com.foodsquad.FoodSquad.model.dto.client;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientProductOption {

    private UUID optionId;

    private String optionName;

    private BigDecimal optionPrice;

    private boolean inStock;
}
