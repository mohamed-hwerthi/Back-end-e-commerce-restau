package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientOrderDTO {
    private List<ClientOrderItemDTO> items;
    private ClientCustomerDTO customer;
    private DeliveryInfoDTO delivery;
    private BigDecimal subtotal;
    private BigDecimal total;


}
