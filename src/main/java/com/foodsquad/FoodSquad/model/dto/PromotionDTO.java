package com.foodsquad.FoodSquad.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionDTO {


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Size(max = 100, message = "Title must be at most 100 characters")
    private String name;

    @NotNull(message = "Discount is required")
    @Min(value = 1, message = "Discount must be at least 1")
    @Max(value = 100, message = "Discount must be at most 100")
    private Integer discountPercentage;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private boolean active;

    @NotBlank(message = "promotion type is required")
    private PromotionType promotionType;



}
