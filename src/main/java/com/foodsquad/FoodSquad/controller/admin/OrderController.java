package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.service.admin.dec.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST controller responsible for managing orders in the admin panel.
 * <p>
 * Provides endpoints for searching and filtering orders based on:
 * - order status code
 * - creation date range
 * - order source (WEBSITE, POS, ADMIN_PANEL)
 * Supports pagination.
 *
 * @since 2025-10-16
 */
@Validated
@RestController
@RequestMapping("/api/orders")
@Tag(name = "4. Order Management", description = "Order Management API for administrators")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Searches orders based on optional filters with pagination.
     *
     * @param statusCode the status code of the order (optional)
     * @param startDate  filter orders created after this date (optional)
     * @param endDate    filter orders created before this date (optional)
     * @param source     filter by order source: WEBSITE, POS, ADMIN_PANEL (optional)
     * @param page       zero-based page index (default: 0)
     * @return a page of matching {@link OrderDTO} wrapped in {@link ResponseEntity}
     */
    @Operation(summary = "Search orders with filters and pagination",
            description = "Retrieve orders filtered by status code, date range, and source, with pagination support.")
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponseDTO<OrderDTO>> searchOrders(
            @RequestParam(required = false) String statusCode,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Searching orders -> statusCode: {}, startDate: {}, endDate: {}, source: {}, page: {}, size: {}",
                statusCode, startDate, endDate, source, page, size);

        Pageable pageable = PageRequest.of(page, size);
        PaginatedResponseDTO<OrderDTO> ordersPage = orderService.searchOrders(statusCode, startDate, endDate, source, pageable);
        return ResponseEntity.ok(ordersPage);
    }


    /**
     * Retrieves a single order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the {@link OrderDTO} representing the order
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID id) {
        log.info("Received request to fetch order with id: {}", id);

        OrderDTO orderDTO = orderService.getOrderById(id);

        log.info("Successfully fetched order with id: {}", id);
        return ResponseEntity.ok(orderDTO);
    }
}