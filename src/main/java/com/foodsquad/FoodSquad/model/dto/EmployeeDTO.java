package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeDTO extends UserDTO {
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    @NotNull(message = "Salary is required")
    private BigDecimal salary;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    private String department;
    
    @NotBlank(message = "Manager ID is required")
    private String managerId;
}
