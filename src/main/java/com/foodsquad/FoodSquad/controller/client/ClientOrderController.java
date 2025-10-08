package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//
//    @Operation(
//            summary = "Get order by ID",
//            description = "Retrieve order details by order ID"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
//            @ApiResponse(responseCode = "404", description = "Order not found")
//    })
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderDTO> getOrderById(
//            @Parameter(description = "Order ID", example = "550e8400-e29b-41d4-a716-446655440000")
//            @PathVariable UUID orderId
//    ) {
//        log.info("Client request: get order by ID: {}", orderId);
//        OrderDTO order = clientOrderService.getOrderById(orderId);
//        return ResponseEntity.ok(order);
//    }
//
//    @Operation(
//            summary = "Get all orders for a customer",
//            description = "Retrieve all orders for a specific customer without pagination"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
//    })
//    @GetMapping("/customer/{customerId}")
//    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(
//            @Parameter(description = "Customer ID", example = "550e8400-e29b-41d4-a716-446655440000")
//            @PathVariable UUID customerId
//    ) {
//        log.info("Client request: get all orders for customer ID: {}", customerId);
//        List<OrderDTO> orders = clientOrderService.getOrdersByCustomerId(customerId);
//        log.info("Found {} orders for customer {}", orders.size(), customerId);
//        return ResponseEntity.ok(orders);
//    }
//
//    @Operation(
//            summary = "Get paginated orders for a customer",
//            description = "Retrieve paginated orders for a specific customer"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
//    })
//    @GetMapping("/customer/{customerId}/pageable")
//    public ResponseEntity<PaginatedResponseDTO<OrderDTO>> getOrdersByCustomerIdPaginated(
//            @Parameter(description = "Customer ID", example = "550e8400-e29b-41d4-a716-446655440000")
//            @PathVariable UUID customerId,
//
//            @Parameter(description = "Page number, starting from 0", example = "0")
//            @RequestParam(defaultValue = "0") int page,
//
//            @Parameter(description = "Number of items per page", example = "10")
//            @RequestParam(defaultValue = "10") int limit
//    ) {
//        log.info("Client request: get paginated orders for customer ID: {} - page: {}, limit: {}",
//                customerId, page, limit);
//        PaginatedResponseDTO<OrderDTO> response = clientOrderService.getOrdersByCustomerId(customerId, page, limit);
//        log.info("Returning {} orders out of total {}", response.getItems().size(), response.getTotalCount());
//        return ResponseEntity.ok(response);
//    }

//    @Operation(
//            summary = "Track order status",
//            description = "Get the current status of an order"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
//            @ApiResponse(responseCode = "404", description = "Order not found")
//    })
//    @GetMapping("/{orderId}/status")
//    public ResponseEntity<String> trackOrderStatus(
//            @Parameter(description = "Order ID", example = "550e8400-e29b-41d4-a716-446655440000")
//            @PathVariable UUID orderId
//    ) {
//        log.info("Client request: track order status for order ID: {}", orderId);
//        String status = clientOrderService.trackOrderStatus(orderId);
//        return ResponseEntity.ok(status);
//    }
//
//    @Operation(
//            summary = "Cancel an order",
//            description = "Cancel an order if it's in a cancellable state (not delivered or already cancelled)"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
//            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled"),
//            @ApiResponse(responseCode = "404", description = "Order not found")
//    })
//    @PutMapping("/{orderId}/cancel")
//    public ResponseEntity<OrderDTO> cancelOrder(
//            @Parameter(description = "Order ID", example = "550e8400-e29b-41d4-a716-446655440000")
//            @PathVariable UUID orderId
//    ) {
//        log.info("Client request: cancel order ID: {}", orderId);
//        OrderDTO cancelledOrder = clientOrderService.cancelOrder(orderId);
//        log.info("Order {} cancelled successfully", orderId);
//        return ResponseEntity.ok(cancelledOrder);
//    }
//
//    @Operation(
//            summary = "Get order history for a customer",
//            description = "Retrieve paginated order history for a customer, sorted by most recent first"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Order history retrieved successfully")
//    })
//    @GetMapping("/customer/{customerId}/history")
//    public ResponseEntity<PaginatedResponseDTO<OrderDTO>> getOrderHistory(
//            @Parameter(description = "Customer ID", example = "550e8400-e29b-41d4-a716-446655440000")
//            @PathVariable UUID customerId,
//
//            @Parameter(description = "Page number, starting from 0", example = "0")
//            @RequestParam(defaultValue = "0") int page,
//
//            @Parameter(description = "Number of items per page", example = "10")
//            @RequestParam(defaultValue = "10") int limit
//    ) {
//        log.info("Client request: get order history for customer ID: {} - page: {}, limit: {}",
//                customerId, page, limit);
//        PaginatedResponseDTO<OrderDTO> response = clientOrderService.getOrderHistory(customerId, page, limit);
//        log.info("Returning {} orders from history", response.getItems().size());
//        return ResponseEntity.ok(response);
//    }
}
