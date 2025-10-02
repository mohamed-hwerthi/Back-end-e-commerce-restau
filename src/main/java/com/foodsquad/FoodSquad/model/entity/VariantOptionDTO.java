package com.foodsquad.FoodSquad.model.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantOptionDTO {
    private UUID id;

    @NotBlank(message = "variant option value cannot be blank")
    private String value;

    @NotNull(message = "variant option price cannot be blank ")
    private BigDecimal price;

    @NotNull(message = "variant option quantity cannot be blank ")
    private String quantity;

    private String sku;

    private UUID productVariantId;
    private UUID variantAttributeID;
}