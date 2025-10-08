package com.foodsquad.FoodSquad.model.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryInfoDTO {
    @NotBlank(message = "address is required")
    private String address;
    private String city;
    private String postalCode;
    private String country;
}