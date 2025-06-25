package com.foodsquad.FoodSquad.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromotionDTO {


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Size(max = 100, message = "Title must be at most 100 characters")
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private boolean active;

    private PromotionType promotionType;

    @NotNull(message = "Discount is required")
    @Min(value = 1, message = "Discount must be at least 1")
    @Max(value = 100, message = "Discount must be at most 100")
    private Integer discountPercentage;


    @Min(value = 1, message = "Discount must be at least 1")
    private  Double      promotionalPrice ;

    private  DiscountType discountType  ;






}
