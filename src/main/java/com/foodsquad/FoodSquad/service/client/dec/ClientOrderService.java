package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for client-facing order operations.
 * Handles order placement, tracking, and retrieval for customers.
 */
public interface ClientOrderService {

    /**
     * Place a new order from a client.
     *
     * @param clientOrderDTO Order details from the client
     * @return Created order DTO with order ID and status
     */
    ClientOrderDTO placeOrder(ClientOrderDTO clientOrderDTO);

    /**
     * Get all orders for a specific customer with pagination.
     *
     * @param customerId ID of the customer
     * @param pageable  Pagination and sorting information
     * @return Paginated response containing order DTOs and pagination metadata
     */
    PaginatedResponseDTO<ClientOrderDTO> getOrdersByCustomer(UUID customerId, Pageable pageable);


    /**
     * Retrieves all orders for a specific cashier session with pagination.
     *
     * @param sessionId The ID of the cashier session
     * @param pageable Pagination information (page number, page size, sort)
     * @return a {@link PaginatedResponseDTO} of {@link OrderDTO} for the session
     */
    PaginatedResponseDTO<ClientOrderDTO> getOrdersBySessionId(UUID sessionId, Pageable pageable);
}
