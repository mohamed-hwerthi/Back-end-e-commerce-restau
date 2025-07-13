package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminDTO extends UserDTO {
    @NotBlank(message = "Admin ID is required")
    private String adminId;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    @NotBlank(message = "Access level is required")
    private String accessLevel;
}
