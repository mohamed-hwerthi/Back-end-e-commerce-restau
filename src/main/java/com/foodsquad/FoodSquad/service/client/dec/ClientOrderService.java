package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;

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


}
