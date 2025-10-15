package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientProductOptionDTO {

    private UUID optionId;

    private String optionName  ;

    private  BigDecimal  optionPrice ;

    private boolean inStock;
}
