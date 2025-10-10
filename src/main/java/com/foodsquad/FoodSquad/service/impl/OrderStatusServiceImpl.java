package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.dto.OrderStatusDTO;
import com.foodsquad.FoodSquad.model.entity.OrderStatus;
import com.foodsquad.FoodSquad.repository.OrderStatusRepository;
import com.foodsquad.FoodSquad.service.OrderStatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;import com.foodsquad.FoodSquad.mapper.OrderStatusMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the OrderStatusService interface.
 * Provides concrete implementations for order status related operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    private final OrderStatusMapper orderStatusMapper;


    @Override
    @Transactional(readOnly = true)
    public Iterable<OrderStatusDTO> getAll() {
        log.info("Fetching all order statuses");
        List<OrderStatus> statuses = orderStatusRepository.findAll();
        return statuses.stream()
                .map(orderStatusMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatus getByCode(String code) {
        log.info("Fetching order status with code: {}", code);
          return  orderStatusRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Order status with code {} not found", code);
                    return new EntityNotFoundException("Order status not found with code: " + code);
                });

    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatusDTO getById(UUID id) {
        log.info("Fetching order status with id: {}", id);
        OrderStatus status = orderStatusRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order status with id {} not found", id);
                    return new EntityNotFoundException("Order status not found with id: " + id);
                });
        return orderStatusMapper.toDto(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderStatusDTO> getAllPaginated(Pageable pageable) {
        log.info("Fetching paginated order statuses with page: {} and size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return orderStatusRepository.findAll(pageable)
                .map(orderStatusMapper::toDto);
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        log.info("Deleting order status with id: {}", id);
        if (!orderStatusRepository.existsById(id)) {
            log.error("Cannot delete. Order status with id {} not found", id);
            throw new EntityNotFoundException("Order status not found with id: " + id);
        }
        try {
            orderStatusRepository.deleteById(id);
            log.info("Successfully deleted order status with id: {}", id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting order status with id: {}. Error: {}", id, e.getMessage());
            return false;
        }
    }
}
