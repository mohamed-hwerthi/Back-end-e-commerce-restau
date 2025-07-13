package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends Employee {
    
    @Column(name = "admin_id", unique = true, nullable = false)
    private String adminId;
    
    @Column(nullable = false)
    private String department;
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @Column(name = "access_level", nullable = false)
    private String accessLevel;
}
