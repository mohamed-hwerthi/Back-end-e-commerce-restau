package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class CashierDTO extends UserDTO {
    @NotBlank(message = "Cashier ID is required")
    private String cashierId;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    @NotNull(message = "Salary is required")
    private BigDecimal salary;
    
    @NotBlank(message = "Shift is required")
    private String shift;
    
    @NotBlank(message = "Manager ID is required")
    private String managerId;
}
