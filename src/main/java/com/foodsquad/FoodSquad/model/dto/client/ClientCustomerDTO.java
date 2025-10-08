package com.foodsquad.FoodSquad.model.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCustomerDTO {
    private UUID id;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private String email;
    @NotBlank(message = "Phone number is required")
    private String phone;
}
