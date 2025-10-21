    package com.foodsquad.FoodSquad.service.client.impl;

    import com.foodsquad.FoodSquad.mapper.AddressMapper;
    import com.foodsquad.FoodSquad.mapper.client.ClientOrderItemMapper;
    import com.foodsquad.FoodSquad.mapper.client.ClientOrderMapper;
    import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
    import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
    import com.foodsquad.FoodSquad.model.dto.client.ClientOrderItemDTO;
    import com.foodsquad.FoodSquad.model.dto.client.ClientOrderItemOptionDTO;
    import com.foodsquad.FoodSquad.model.entity.*;
    import com.foodsquad.FoodSquad.repository.OrderRepository;
    import com.foodsquad.FoodSquad.service.CashierSessionService;
    import com.foodsquad.FoodSquad.service.OrderStatusService;
    import com.foodsquad.FoodSquad.service.ProductOptionService;
    import com.foodsquad.FoodSquad.service.admin.dec.ProductService;
    import com.foodsquad.FoodSquad.service.client.dec.ClientCustomerService;
    import com.foodsquad.FoodSquad.service.client.dec.ClientOrderService;
    import jakarta.persistence.EntityNotFoundException;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.util.ObjectUtils;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;
    import java.util.UUID;
    import java.util.stream.Collectors;

    import static com.foodsquad.FoodSquad.config.utils.Constant.ORDER_STATUS_PENDING;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class ClientOrderServiceImpl implements ClientOrderService {

        private final OrderRepository orderRepository;

        private final ClientOrderMapper clientOrderMapper;

        private final ClientCustomerService clientCustomerService;

        private final OrderStatusService orderStatusService;

        private final ProductService productService;

        private final AddressMapper addressMapper;

        private final ProductOptionService productOptionService;

        private final ClientOrderItemMapper clientOrderItemMapper;

        private final CashierSessionService cashierSessionService ;




        @Override
        @Transactional
        public ClientOrderDTO placeOrder(ClientOrderDTO clientOrderDTO) {
            log.info("Placing new order for customer");
            Customer customer = null;
            if (!ObjectUtils.isEmpty(clientOrderDTO.getCustomer()) && !clientOrderDTO.getSource().equals(OrderSource.POS)) {
                customer = clientCustomerService.findOrCreateCustomerFromOrder(clientOrderDTO);
            }
            OrderStatus orderStatus = orderStatusService.getByCode(ORDER_STATUS_PENDING);
            Order order = buildOrder(clientOrderDTO, customer, orderStatus);
            order.calculateTotal();
            order.setOrderNumber(generateOrderNumber());
            if (OrderSource.POS.equals(clientOrderDTO.getSource())) {
                CashierSession session = cashierSessionService.getSession(clientOrderDTO.getCashierSessionId());
                order.setCashierSession(session);
                order.setCashReceived(clientOrderDTO.getCashReceived());
                order.setChangeGiven(calculateChange(order.getTotal(), clientOrderDTO.getCashReceived()));
                order.setIsPrinted(false);
            }

            order.setSource(OrderSource.WEBSITE);
            Order savedOrder = orderRepository.save(order);

            log.info("Order placed successfully with ID: {}", savedOrder.getId());
            return clientOrderMapper.toDto(savedOrder);
        }

        @Override
        public PaginatedResponseDTO<ClientOrderDTO> getOrdersByCustomer(UUID customerId, Pageable pageable) {
            log.info("Fetching orders for customer with ID: {}", customerId);
            if (!clientCustomerService.existsById(customerId)) {
                throw  new   EntityNotFoundException ("Customer not found with ID: " + customerId);
            }

            Page<Order> orderPage = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId, pageable);
            List<ClientOrderDTO> orderDTOs = orderPage.getContent().stream()
                    .map(clientOrderMapper::toDto)
                    .collect(Collectors.toList());

            log.info("Found {} orders for customer with ID: {}", orderDTOs.size(), customerId);
            return new PaginatedResponseDTO<>(
                orderDTOs,
                orderPage.getTotalElements()
            );
        }

        /**
         * Build an Order entity from DTO and customer.
         */
        private Order buildOrder(ClientOrderDTO dto, Customer customer, OrderStatus status) {
            Order order = initOrder(dto, customer, status);

            dto.getOrderItems().forEach(itemDTO -> {
                OrderItem orderItem = buildOrderItem(itemDTO);
                order.addOrderItem(orderItem);
            });

            return order;
        }

        /**
         * Initialize a new Order with basic info.
         */
        private Order initOrder(ClientOrderDTO dto, Customer customer, OrderStatus status) {
            return Order.builder()
                    .customer(customer)
                    .createdAt(LocalDateTime.now())
                    .status(status)
                    .deliveryAddress(addressMapper.toEntity(dto.getDeliveryAddress()))
                    .orderItems(new ArrayList<>())
                    .total(dto.getTotal())
                    .subTotal(dto.getSubTotal())
                    .build();
        }

        /**
         * Build an OrderItem entity including options.
         */
        private OrderItem buildOrderItem(ClientOrderItemDTO itemDTO) {
            Product product = productService.findProductById(itemDTO.getProductId());
            OrderItem orderItem = clientOrderItemMapper.toEntity(itemDTO);
            orderItem.setProduct(product);

            if (itemDTO.getOptions() != null && !itemDTO.getOptions().isEmpty()) {
                itemDTO.getOptions().forEach(optionDTO -> {
                    OrderItemOption option = buildOrderItemOption(optionDTO);
                    orderItem.addOption(option);
                });
            }

            return orderItem;
        }


               /**
         * Build an OrderItemOption entity from DTO.
         */
        private OrderItemOption buildOrderItemOption(ClientOrderItemOptionDTO optionDTO) {
            ProductOption productOption = productOptionService.getById(optionDTO.getOptionId());

            return OrderItemOption.builder()
                    .productOption(productOption)
                    .build();
        }


        /*
        todo   :to change it after by good logic with counter  , date of today ...
         */
        public String generateOrderNumber() {
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random = new Random();
            StringBuilder sb = new StringBuilder("ORD-");
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            return sb.toString();
        }


        private BigDecimal calculateChange(BigDecimal total, BigDecimal cashReceived) {
            if (cashReceived == null) return BigDecimal.ZERO;
            return cashReceived.subtract(total);
        }


    }
