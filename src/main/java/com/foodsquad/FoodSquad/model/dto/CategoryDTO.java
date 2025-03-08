package com.foodsquad.FoodSquad.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {
   @JsonProperty(access =  JsonProperty.Access.READ_ONLY)
    private  Long id  ;
    @NotBlank(message = "name  cannot be blank")
    private String  name  ;
    private String description  ;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

}
