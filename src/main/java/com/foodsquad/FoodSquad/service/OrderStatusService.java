package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.OrderStatusDTO;
import com.foodsquad.FoodSquad.model.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for managing OrderStatus entities.
 * Provides methods for retrieving and managing order statuses.
 */
public interface OrderStatusService {
    
    /**
     * Retrieves all order statuses.
     * 
     * @return Iterable of OrderStatusDto containing all order statuses
     */
    Iterable<OrderStatusDTO> getAll();
    
    /**
     * Retrieves an order status by its unique code.
     * 
     * @param code The code of the order status to retrieve
     * @return OrderStatusDTO containing the order status details
     */
    OrderStatus getByCode(String code);
    
    /**
     * Retrieves an order status by its ID.
     * 
     * @param id The ID of the order status to retrieve
     * @return OrderStatusDTO containing the order status details
     */
    OrderStatusDTO getById(UUID id);
    
    /**
     * Retrieves a paginated list of order statuses.
     * 
     * @param pageable Pagination information
     * @return Page of OrderStatusDTO containing the paginated results
     */
    Page<OrderStatusDTO> getAllPaginated(Pageable pageable);
    
    /**
     * Deletes an order status by its ID.
     * 
     * @param id The ID of the order status to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(UUID id);
}
