package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private UserRole role;
    private String imageUrl;
    
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Phone number should be in format: 000-000-0000")
    private String phoneNumber;
}
