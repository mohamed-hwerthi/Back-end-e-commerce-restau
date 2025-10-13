package com.foodsquad.FoodSquad.model.dto.client;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderItemOptionDTO {

    @NotNull(message = "order item option id  cannot be blank ")
    private UUID optionId;
    private String optionName;
    private BigDecimal optionPrice;
}
