package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientOrderItemDTO {

    private UUID productId;
    private BigDecimal price;
    private int quantity;
    private List<ClientOptionDTO> options;
    private BigDecimal totalPrice;
}
