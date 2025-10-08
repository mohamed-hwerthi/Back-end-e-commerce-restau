package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.mapper.OrderMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;
import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.model.entity.OrderItem;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.OrderStatusRepository;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.service.admin.dec.CustomerService;
import com.foodsquad.FoodSquad.service.client.dec.ClientOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientOrderServiceImpl implements ClientOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderMapper orderMapper;
    private final CustomerService customerService;

    @Override
    @Transactional
    public ClientOrderDTO placeOrder(ClientOrderDTO clientOrderDTO) {
        log.info("Placing new order for customer");

        Customer customer = customerService.findOrCreateCustomerFromOrder(clientOrderDTO);

        Order order = buildOrder(clientOrderDTO, customer);
        Order savedOrder = orderRepository.save(order);

        log.info("Order placed successfully with ID: {}", savedOrder.getId());

        return orderMapper.toClientOrderDTO(savedOrder);
    }


    /**
     * Builds an Order entity from the ClientOrderDTO and associated Customer.
     */
    private Order buildOrder(ClientOrderDTO clientOrderDTO, Customer customer) {
        Order order = Order.builder()
                .customer(customer)
                .createdAt(LocalDateTime.now())
                .total(clientOrderDTO.getTotal())
                .build();

        clientOrderDTO.getOrderItems().forEach(itemDTO -> {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemDTO.getProductId()));

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(itemDTO.getPrice())
                    .build();

            order.addOrderItem(orderItem);
        });

        return order;
    }
}
