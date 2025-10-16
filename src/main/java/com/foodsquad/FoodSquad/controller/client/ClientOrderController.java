package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.dto.common.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Client-facing Order endpoints.
 * Handles order placement, tracking, and history for customers.
 */
@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Client Order Management", description = "Client Order Management API")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @Operation(
            summary = "Place a new order",
            description = "Create a new order with customer details, items, and delivery information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    @PostMapping
    public ResponseEntity<ClientOrderDTO> placeOrder(@Valid @RequestBody ClientOrderDTO clientOrderDTO) {
        log.info("Client request: place new order");
        ClientOrderDTO createdOrder = clientOrderService.placeOrder(clientOrderDTO);
        log.info("Order placed successfully with ID: {}", createdOrder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @Operation(
            summary = "Get orders by customer ID",
            description = "Retrieve paginated list of orders for a specific customer, sorted by creation date descending"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<PaginatedResponseDTO<ClientOrderDTO>> getOrdersByCustomer(
            @Parameter(description = "ID of the customer", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID customerId,
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(
                page = 0,
                size = 20,
                sort = "createdAt",
                direction = Sort.Direction.DESC
            ) Pageable pageable) {
        
        log.info("Fetching orders for customer ID: {}", customerId);
        PaginatedResponseDTO<ClientOrderDTO> response = clientOrderService.getOrdersByCustomer(customerId, pageable);
        return ResponseEntity.ok(response);
    }

}
