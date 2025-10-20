package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.OrderStatusDTO;
import com.foodsquad.FoodSquad.service.OrderStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing order statuses.
 * Provides endpoints for retrieving and managing order statuses.
 */
@RestController
@RequestMapping("/api/order-statuses")
@Tag(name = "Order Status", description = "APIs for managing order statuses")
@Slf4j
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    /**
     * Constructs a new OrderStatusController with the required service.
     *
     * @param orderStatusService The service for order status operations
     */
    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    /**
     * GET /api/order-statuses : Get all order statuses.
     *
     * @return ResponseEntity with status 200 (OK) and the list of all order statuses
     */
    @GetMapping
    @Operation(summary = "Get all order statuses", description = "Retrieves a list of all order statuses")
    public ResponseEntity<Iterable<OrderStatusDTO>> getAllOrderStatuses() {
        log.info("REST request to get all order statuses");
        return ResponseEntity.ok(orderStatusService.getAll());
    }



    /**
     * GET /api/order-statuses/{id} : Get order status by ID.
     *
     * @param id The ID of the order status to retrieve
     * @return ResponseEntity with status 200 (OK) and the order status, or 404 (Not Found) if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order status by ID", description = "Retrieves an order status by its ID")
    public ResponseEntity<OrderStatusDTO> getOrderStatusById(@PathVariable UUID id) {
        log.info("REST request to get order status by id: {}", id);
        return ResponseEntity.ok(orderStatusService.getById(id));
    }

    /**
     * GET /api/order-statuses/paginated : Get paginated order statuses.
     *
     * @param pageable Pagination information
     * @return ResponseEntity with status 200 (OK) and the paginated list of order statuses
     */
    @GetMapping("/paginated")
    @Operation(summary = "Get paginated order statuses", 
               description = "Retrieves a paginated list of order statuses")
    public ResponseEntity<Page<OrderStatusDTO>> getPaginatedOrderStatuses(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get paginated order statuses with page: {}, size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(orderStatusService.getAllPaginated(pageable));
    }

    /**
     * DELETE /api/order-statuses/{id} : Delete an order status by ID.
     *
     * @param id The ID of the order status to delete
     * @return ResponseEntity with status 204 (No Content) if successful, or 404 (Not Found) if not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order status", 
               description = "Deletes an order status by its ID")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable UUID id) {
        log.info("REST request to delete order status with id: {}", id);
        orderStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
