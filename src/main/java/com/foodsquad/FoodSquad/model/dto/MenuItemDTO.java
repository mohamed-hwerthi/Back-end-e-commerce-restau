package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.MenuItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;


public class MenuItemDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @URL
    @NotBlank(message = "ImageUrl cannot be blank")
    @Schema(defaultValue = "https://www.tastingtable.com/img/gallery/what-makes-restaurant-burgers-taste-different-from-homemade-burgers-upgrade/l-intro-1662064407.jpg")
    private String imageUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean defaultItem;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    @Schema(defaultValue = "1", required = true)
    private Double price;

    @NotNull(message = "Category cannot be null")
    @Schema(defaultValue = "BURGER")
    private MenuItemCategory category;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer salesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long reviewCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double averageRating;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "a menu item must have at least one category")
    private List<Long> categoriesIds= new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CategoryDTO>categoryDTOS = new ArrayList<>() ;

    public MenuItemDTO() {

    }

    public MenuItemDTO(MenuItem menuItem, int salesCount, long reviewCount, double averageRating) {

        this.id = menuItem.getId();
        this.title = menuItem.getTitle();
        this.description = menuItem.getDescription();
        this.imageUrl = menuItem.getImageUrl();
        this.defaultItem = menuItem.getDefaultItem();
        this.price = menuItem.getPrice();
        this.category = menuItem.getCategory();
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
    }


    public Long getId() {

        return id;
    }

    public @NotBlank(message = "Title cannot be blank") String getTitle() {

        return title;
    }

    public void setTitle(@NotBlank(message = "Title cannot be blank") String title) {

        this.title = title;
    }

    public @NotBlank(message = "Description cannot be blank") String getDescription() {

        return description;
    }

    public void setDescription(@NotBlank(message = "Description cannot be blank") String description) {

        this.description = description;
    }

    public @URL @NotBlank(message = "ImageUrl cannot be blank") String getImageUrl() {

        return imageUrl;
    }

    public void setImageUrl(@URL @NotBlank(message = "ImageUrl cannot be blank") String imageUrl) {

        this.imageUrl = imageUrl;
    }

    public Boolean getDefaultItem() {

        return defaultItem;
    }

    public void setDefaultItem(Boolean defaultItem) {

        this.defaultItem = defaultItem;
    }

    public @NotNull(message = "Price cannot be null") @Positive(message = "Price must be positive") Double getPrice() {

        return price;
    }

    public void setPrice(@NotNull(message = "Price cannot be null") @Positive(message = "Price must be positive") Double price) {

        this.price = price;
    }

    public void setId(Long id) {

        this.id = id;
    }


    public void setCategory(MenuItemCategory category) {

        this.category = category;
    }

    public MenuItemCategory getCategory() {

        return category;
    }

    public void setCategory(String category) {

        if (category != null) {
            this.category = MenuItemCategory.valueOf(category.toUpperCase());
        } else {
            throw new IllegalArgumentException("Category cannot be null");
        }
    }

    public Integer getSalesCount() {

        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {

        this.salesCount = salesCount;
    }

    public Long getReviewCount() {

        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {

        this.reviewCount = reviewCount;
    }

    public Double getAverageRating() {

        return averageRating;
    }

    public void setAverageRating(Double averageRating) {

        this.averageRating = averageRating;
    }

    public List<Long> getCategoriesIds() {

        return categoriesIds;
    }

    public void setCategoriesIds(List<Long> categoriesIds) {

        this.categoriesIds = categoriesIds;
    }

    public List<CategoryDTO> getCategoryDTOS() {

        return categoryDTOS;
    }

    public void setCategoryDTOS(List<CategoryDTO> categoryDTOS) {

        this.categoryDTOS = categoryDTOS;
    }

}
