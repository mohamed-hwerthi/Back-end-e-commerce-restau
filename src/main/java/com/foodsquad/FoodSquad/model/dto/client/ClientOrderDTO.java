package com.foodsquad.FoodSquad.model.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodsquad.FoodSquad.model.dto.AddressDTO;
import com.foodsquad.FoodSquad.model.entity.OrderSource;
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
public class ClientOrderDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotEmpty(message = "At Least an item is required")
    @Valid
    private List<ClientOrderItemDTO> orderItems;

    @Valid
    @NotNull(message = "customer can not be null ")
    private ClientCustomerDTO customer;

    @NotNull(message = "Total is required")
    @Positive(message = "Order total must be greater than zero")
    private BigDecimal total;

    @NotNull(message = "Total is required")
    @Positive(message = "Order total must be greater than zero")
    private BigDecimal subTotal;

    @NotNull
    @Valid
    private AddressDTO deliveryAddress;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    private OrderSource source;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String orderNumber;

    private UUID cashierSessionId;

    private BigDecimal cashReceived;

    private BigDecimal changeGiven;


}