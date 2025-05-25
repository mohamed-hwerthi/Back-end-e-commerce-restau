package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodsquad.FoodSquad.model.entity.MenuItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuItemDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String title;

    private String description;

    private String barCode;

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

    private boolean isPromoted  ;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)


    private   Double discountedPrice  ;


    private CurrencyDTO currency;



    private TaxDTO tax;


    public MenuItemDTO() {

    }



}