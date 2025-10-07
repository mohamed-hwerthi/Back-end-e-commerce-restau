package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryInfoDTO {
    private String address;
    private String city;
    private String postalCode;
    private String country;
}