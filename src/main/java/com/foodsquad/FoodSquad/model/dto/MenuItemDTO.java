package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuItemDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotBlank(message = "code bar can not be null ")
    private String barCode;


    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    @Schema(defaultValue = "1", required = true)
    private Double price;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer salesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long reviewCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double averageRating;
    @NotEmpty(message = "product must has a category ")
    private List<CategoryDTO>categories = new ArrayList<>() ;
    private List<MediaDTO>medias = new ArrayList<>() ;

    public MenuItemDTO() {

    }

    public MenuItemDTO(MenuItem menuItem, int salesCount, long reviewCount, double averageRating, List<CategoryDTO> categories, List<MediaDTO> mediaDTOS) {

        this.id = menuItem.getId();
        this.title = menuItem.getTitle();
        this.medias = mediaDTOS;
        this.barCode = menuItem.getBarCode();
        this.description = menuItem.getDescription();
        this.categories = categories;
        this.price = menuItem.getPrice();
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
    }


}