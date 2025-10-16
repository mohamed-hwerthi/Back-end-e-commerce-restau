package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Find customer by phone number
     */
    Optional<Customer> findByPhone(String phone);

    /**
     * Check if a customer with the given email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if a customer with the given phone number exists
     */
    boolean existsByPhone(String phone);

    /**
     * Find customer by email with case-insensitive search
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.email) = LOWER(:email)")
    Optional<Customer> findByEmailIgnoreCase(@Param("email") String email);
}
