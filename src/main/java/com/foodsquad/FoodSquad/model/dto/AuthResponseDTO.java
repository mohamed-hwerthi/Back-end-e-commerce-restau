package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.UserRole;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponseDTO {
    private UUID id;
    private String email;
    private UserRole role;
    private String storeId;
}
