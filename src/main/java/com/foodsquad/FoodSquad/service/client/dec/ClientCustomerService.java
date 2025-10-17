package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.dto.client.CustomerLoginDTO;
import com.foodsquad.FoodSquad.model.dto.client.CustomerRegistrationDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;

import java.util.UUID;

/**
 * Service interface for managing customers.
 * Provides methods for CRUD operations and querying customers.
 */
public interface ClientCustomerService {

    /**
     * Register a new customer.
     *
     * @param registrationDTO DTO containing customer registration details
     * @return Registered customer entity
     */
    Customer registerCustomer(CustomerRegistrationDTO registrationDTO);

    /**
     * Register a guest customer during checkout.
     *
     * @param email Customer's email
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param phone Customer's phone number
     * @return The created or existing guest customer
     */
    Customer registerGuestCustomer(String email, String firstName, String lastName, String phone);

    /**
     * Convert a guest customer to a registered customer.
     *
     * @param email Guest's email
     * @param registrationDTO Registration details
     * @return The updated customer
     */
    Customer convertGuestToRegistered(String email, CustomerRegistrationDTO registrationDTO);

    /**
     * Authenticate a customer.
     *
     * @param loginDTO DTO containing login credentials
     * @return Authenticated customer entity if successful, null otherwise
     */
  //  Customer loginCustomer(CustomerLoginDTO loginDTO);

    /**
     * Find an existing customer by ID, email, or phone, or create a new guest customer.
     * Used by ClientOrderService when placing an order.
     *
     * @param clientOrderDTO DTO containing customer info.
     * @return Existing or newly created Customer entity (as guest if new).
     */
    Customer findOrCreateCustomerFromOrder(ClientOrderDTO clientOrderDTO);

    /**
     * Check if a customer exists by ID.
     *
     * @param customerId ID of the customer to check
     * @return true if customer exists, false otherwise
     */
    boolean existsById(UUID customerId);
}
