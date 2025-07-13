package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    private String id;
    
    @NotBlank(message = "Code cannot be blank")
    private String code;
    
    @NotBlank(message = "Name cannot be blank")
    private String name;
}
