package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;
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

    @JsonProperty("isOption")
    private boolean isOption;

    private boolean isVariant;

    @PositiveOrZero(message = "Quantity must be zero or a positive number")
    private int quantity;

    private String sku;

    @Positive(message = "Purchase price must be greater than zero")
    private BigDecimal purchasePrice;

    @PositiveOrZero(message = "Price must be zero or a positive number")
    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer salesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long reviewCount;

    @PositiveOrZero(message = "Low stock threshold must be zero or a positive number")
    private int lowStockThreshold;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double averageRating;

    private List<CategoryDTO> categories = new ArrayList<>();

    private BigDecimal discountedPrice;

    private List<MediaDTO> medias = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isPromoted;

    private TaxDTO tax;

    private List<VariantDTO> variants = new ArrayList<>();

    private List<ProductAttributeDTO> availableAttributes = new ArrayList<>();

    private List<ProductOptionGroupDTO> productOptionGroups = new ArrayList<>();

    private List<CustomAttributeDTO> customAttributes = new ArrayList<>();
}
