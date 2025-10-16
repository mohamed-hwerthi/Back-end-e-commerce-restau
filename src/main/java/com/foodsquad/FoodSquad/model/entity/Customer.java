package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a customer in the system.
 * Can be either a registered user (isGuest = false) or a guest user (isGuest = true).
 * Guest users are created during checkout without requiring full registration.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends Partner {

    @Column(name = "is_guest", nullable = false, columnDefinition = "boolean default false")
    private boolean isGuest;

    /**
     * Converts a guest customer to a registered customer.
     * Call this method when a guest decides to register.
     * 
     * @param password The password to set for the new registered user
     * @return The updated customer
     */
    public Customer convertToRegisteredUser(String password) {
        this.isGuest = false;
        this.setPassword(password);
        return this;
    }

    /**
     * Creates a new guest customer with minimal required information.
     * 
     * @param email Customer's email
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param phone Customer's phone number
     * @return A new guest Customer instance
     */
    public static Customer createGuest(String email, String firstName, String lastName, String phone) {
        return Customer.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .isGuest(true)
                .build();
    }
}
