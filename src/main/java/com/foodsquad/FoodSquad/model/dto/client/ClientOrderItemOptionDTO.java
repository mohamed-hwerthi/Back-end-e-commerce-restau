package com.foodsquad.FoodSquad.model.dto.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "order item option id  cannot be blank ")
    private UUID optionId;
    @NotBlank(message = "order item option name cannot be blank ")
    private String optionName;
    @NotNull(message = "order item option  price can not be null")
    private BigDecimal optionPrice;
}
