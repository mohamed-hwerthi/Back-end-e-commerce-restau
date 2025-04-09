package com.foodsquad.FoodSquad.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CategoryDTO {
    private  Long id  ;
    @NotBlank(message = "name  cannot be blank")
    private String  name  ;
    private String description  ;
    private List<MediaDTO> medias  = new ArrayList<>() ;
}
