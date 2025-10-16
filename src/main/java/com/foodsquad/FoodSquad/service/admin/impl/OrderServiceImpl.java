package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.mapper.OrderMapper;
import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.service.admin.dec.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public PaginatedResponseDTO<OrderDTO> searchOrders(String statusCode,
                                                       LocalDateTime startDate,
                                                       LocalDateTime endDate,
                                                       String source,
                                                       Pageable pageable) {
        log.info("Searching orders with filters -> statusCode: {}, startDate: {}, endDate: {}, source: {}, page: {}, size: {}",
                statusCode, startDate, endDate, source, pageable.getPageNumber(), pageable.getPageSize());

        Page<Order> ordersPage;

        boolean noFilters = (statusCode == null || statusCode.isBlank()) &&
                startDate == null &&
                endDate == null &&
                (source == null || source.isBlank());

        if (noFilters) {
            log.info("No filters provided — fetching all orders paginated.");
            ordersPage = orderRepository.findAll(pageable);
        } else {
            log.info("Applying filters to query orders...");
            ordersPage = orderRepository.findOrdersByFilters(statusCode, startDate, endDate, source, pageable);
        }

        List<OrderDTO> responseDTOS = orderMapper.toDtoList(ordersPage.getContent());
        return new PaginatedResponseDTO<>(responseDTOS, ordersPage.getTotalElements());
    }

    @Override
    public OrderDTO getOrderById(UUID id) {
        log.info("Fetching order with id: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        return orderMapper.toDto(order);
    }

}
