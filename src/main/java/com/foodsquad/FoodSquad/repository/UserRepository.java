package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * Checks if a user exists with the given email or phone number.
     *
     * @param email       the email to check
     * @param phoneNumber the phone number to check
     * @return true if a user exists with either email or phone number
     */
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
}