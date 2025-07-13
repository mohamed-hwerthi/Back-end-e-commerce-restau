package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Cashier extends Employee {
    
    @Column(name = "cashier_id", unique = true, nullable = false)
    private String cashierId;
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @Column(nullable = false)
    private BigDecimal salary;
    
    @Column(nullable = false)
    private String shift;
    
    @Column(name = "manager_id", nullable = false)
    private String managerId;
}
