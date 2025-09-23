package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.VariantOptionDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductVariantDTO {

    private UUID id  ;
    @NotBlank(message = "price has to be not null ")
    @PositiveOrZero(message = "price has to be positive ")
    private BigDecimal price;
    private String sku;
    @NotBlank(message = "price has to be not null ")
    @PositiveOrZero(message = "price has to be positive ")
    private Integer quantity;
    private List<VariantOptionDTO> options = new ArrayList<>();
}
