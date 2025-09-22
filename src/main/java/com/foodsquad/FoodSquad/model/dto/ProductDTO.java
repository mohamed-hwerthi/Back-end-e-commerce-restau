package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private LocalizedString title;

    private LocalizedString description;

    private String barCode;
    @PositiveOrZero(message = "La quantité doit être positive ou zéro")
    private int quantity;

    @Positive(message = "Purchase price must be positive")
    private BigDecimal purchasePrice;

    @PositiveOrZero(message = "Le prix  doit être positive ou zéro")
    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer salesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long reviewCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double averageRating;
    private List<CategoryDTO> categories = new ArrayList<>();

    private BigDecimal discountedPrice ;

    private List<MediaDTO> medias = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private boolean isPromoted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    private TaxDTO tax;

    private List<ProductVariantDTO>variants = new ArrayList<>() ;



}