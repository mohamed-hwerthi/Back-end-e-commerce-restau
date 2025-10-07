package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCustomerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
