package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository ProductRepository;

    private final ProductService ProductService;

    private final ModelMapper modelMapper;


    public ResponseEntity<OrderDTO> createOrder(OrderDTO orderDTO) {
        validateOrderDTO(orderDTO);

        User user = fetchUser(orderDTO.getUserEmail());
        Map<Product, Integer> ProductsWithQuantity = buildProducts(orderDTO);
        BigDecimal totalCost = calculateTotalCost(ProductsWithQuantity);

        Order order = new Order();
        order.setUser(user);
        order.setProductsWithQuantity(ProductsWithQuantity);
        order.setStatus(OrderStatus.valueOf(orderDTO.getStatus().toUpperCase()));
        order.setTotalCost(totalCost);
        order.setCreatedAt(orderDTO.getCreatedOn());
        order.setPaid(true);

        orderRepository.save(order);

        OrderDTO responseDTO = modelMapper.map(order, OrderDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    public List<OrderDTO> getAllOrders(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdOn"));
        Page<Order> orderPage = orderRepository.findAllOrdersWithUsers(pageable);
        return orderPage.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByUserId(UUID userId, int page, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        checkOwnership(user);
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findOrdersByUserId(userId, pageable);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public ResponseEntity<OrderDTO> getOrderById(String id) {

        Order order = orderRepository.findOrderWithUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + id));
        checkOwnership(order.getUser());
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        return ResponseEntity.ok(orderDTO);
    }

    public ResponseEntity<OrderDTO> updateOrder(String id, OrderDTO orderDTO) {
        validateOrderDTO(orderDTO);

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + id));
        checkOwnership(existingOrder.getUser());

        User user = fetchUser(orderDTO.getUserEmail());
        Map<Product, Integer> ProductsWithQuantity = buildProducts(orderDTO);
        BigDecimal totalCost = calculateTotalCost(ProductsWithQuantity);

        existingOrder.setUser(user);
        existingOrder.setProductsWithQuantity(ProductsWithQuantity);
        existingOrder.setStatus(OrderStatus.valueOf(orderDTO.getStatus().toUpperCase()));
        existingOrder.setTotalCost(totalCost);
        existingOrder.setCreatedAt(orderDTO.getCreatedOn());
        existingOrder.setPaid(orderDTO.getPaid());

        orderRepository.save(existingOrder);

        OrderDTO updatedOrderDTO = modelMapper.map(existingOrder, OrderDTO.class);
        return ResponseEntity.ok(updatedOrderDTO);
    }


    public ResponseEntity<Map<String, String>> deleteOrder(String id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + id));
        checkOwnership(order.getUser());
        orderRepository.delete(order);
        return ResponseEntity.ok(Map.of("message", "Order successfully deleted"));
    }

    public ResponseEntity<Map<String, String>> deleteOrders(List<String> ids) {

        List<Order> orders = orderRepository.findAllById(ids);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("No Orders found for the given IDs");
        }
        for (Order order : orders) {
            checkOwnership(order.getUser());
        }
        orderRepository.deleteAll(orders);
        return ResponseEntity.ok(Map.of("message", "Orders successfully deleted"));
    }

    public Order getSimpleOrderById(String OrderId) {

        return orderRepository.findById(OrderId).orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + OrderId));

    }

    private User getCurrentUser() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void checkOwnership(User owner) {

        User currentUser = getCurrentUser();
        if (!currentUser.equals(owner) && !currentUser.getRole().equals(UserRole.ADMIN) && !currentUser.getRole().equals(UserRole.EMPLOYEE)) {
            throw new IllegalArgumentException("Access denied");
        }
    }

    private void validateOrderDTO(OrderDTO orderDTO) {
        if (orderDTO.getProductQuantities() == null || orderDTO.getProductQuantities().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one menu item");
        }
    }

    private User fetchUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    private Map<Product, Integer> buildProducts(OrderDTO orderDTO) {
        Map<Product, Integer> ProductsWithQuantity = new HashMap<>();

        for (Map.Entry<UUID, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
            UUID ProductId = entry.getKey();
            int quantity = (entry.getValue() != null && entry.getValue() > 0) ? entry.getValue() : 1;

            Product product = ProductRepository.findById(ProductId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid menu item ID: " + ProductId));

            ProductService.decrementProductQuantity(product.getId(), quantity);
            ProductsWithQuantity.put(product, quantity);
        }

        return ProductsWithQuantity;
    }

    private BigDecimal calculateTotalCost(Map<Product, Integer> ProductsWithQuantity) {
        return ProductsWithQuantity.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }


}
