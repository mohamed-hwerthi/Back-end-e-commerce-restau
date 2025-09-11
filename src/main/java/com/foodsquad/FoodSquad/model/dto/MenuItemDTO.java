package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class MenuItemDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String title;

    private String description;

    private String barCode;
    @Positive(message = "Quantity must be positive")
    private int quantity;

    @Positive(message = "Purchase price must be positive")
    private BigDecimal purchasePrice;

    @Positive(message = "Price must be positive")
    private Double price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer salesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long reviewCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double averageRating;
    private List<CategoryDTO> categories = new ArrayList<>();

    private List<MediaDTO> medias = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private boolean isPromoted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private Double discountedPrice;

    private TaxDTO tax;


    public MenuItemDTO() {

    }


}