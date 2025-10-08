package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private UUID id;

    @NotNull(message = "Customer information cannot be null")
    @Valid
    private CustomerDTO customerDTO;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemDTO> orderItems;

    @NotNull(message = "Delivery address cannot be null")
    @Valid
    private AddressDTO deliveryAddress;

    private LocalDateTime createdAt;

    @NotNull(message = "Order status cannot be null")
    private OrderStatusDTO status;

    @NotNull(message = "Total amount cannot be null")
    @Positive(message = "Total amount must be greater than zero")
    private BigDecimal total;

    @NotNull(message = "Subtotal cannot be null")
    @Positive(message = "Subtotal must be greater than zero")
    private BigDecimal subtotal;
}
