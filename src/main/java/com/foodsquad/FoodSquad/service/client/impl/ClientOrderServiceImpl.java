package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.mapper.AddressMapper;
import com.foodsquad.FoodSquad.mapper.OrderMapper;
import com.foodsquad.FoodSquad.mapper.client.ClientOrderItemMapper;
import com.foodsquad.FoodSquad.mapper.client.ClientOrderMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.*;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.service.OrderStatusService;
import com.foodsquad.FoodSquad.service.admin.dec.ProductService;
import com.foodsquad.FoodSquad.service.client.dec.ClientCustomerService;
import com.foodsquad.FoodSquad.service.client.dec.ClientOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    private final ClientOrderItemMapper clientOrderItemMapper ;


    @Override
    @Transactional
    public ClientOrderDTO placeOrder(ClientOrderDTO clientOrderDTO) {
        log.info("Placing new order for customer");

        Customer customer = clientCustomerService.findOrCreateCustomerFromOrder(clientOrderDTO);

        OrderStatus orderStatus = orderStatusService.getByCode(ORDER_STATUS_PENDING);

        Order order = buildOrder(clientOrderDTO, customer, orderStatus);

        order.calculateTotal();


        Order savedOrder = orderRepository.save(order);

        log.info("Order placed successfully with ID: {}", savedOrder.getId());

        return clientOrderMapper.toClientOrderDTO(savedOrder);
    }

    /**
     * Build an Order entity from DTO and customer.
     */
        private Order buildOrder(ClientOrderDTO dto, Customer customer, OrderStatus status) {
            Order order = Order.builder()
                    .customer(customer)
                    .createdAt(LocalDateTime.now())
                    .status(status)
                    .deliveryAddress(addressMapper.toEntity(dto.getDeliveryAddress()))
                    .orderItems(new ArrayList<>())
                    .total(dto.getTotal())
                    .subTotal(dto.getSubTotal())
                    .build();

        for (var itemDTO : dto.getOrderItems()) {
            Product product = productService.findProductById(itemDTO.getProductId());

            OrderItem orderItem = clientOrderItemMapper.toEntity(itemDTO) ;
            orderItem.setProduct(product);


            order.addOrderItem(orderItem);
        }

        return order;
    }

}
