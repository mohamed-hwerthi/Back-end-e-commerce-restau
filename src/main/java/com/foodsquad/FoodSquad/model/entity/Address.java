package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Postal code cannot be blank")
    private String postalCode;

    @ManyToOne(optional = false)
    private Country country;
}
