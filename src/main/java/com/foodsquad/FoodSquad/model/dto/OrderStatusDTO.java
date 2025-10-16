package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.validation.annotations.NotEmptyLocalizedString;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusDTO {


    private UUID id;

    @NotNull(message = "order status code must be not nul")
    private String code;

    @NotEmptyLocalizedString(message = "order status name must be not nul")
    private LocalizedString name;

    private String color  ;
}
