package com.foodsquad.FoodSquad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductOptionDTO {

    private UUID id;

    @Min(value = 0, message = "Price must be at least 0")
    private BigDecimal overridePrice;

    @NotBlank(message = "the linked product id cannot be blank ")
    private UUID linkedProductId;

}
