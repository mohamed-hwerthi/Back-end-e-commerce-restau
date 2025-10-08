package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDTO {
    private UUID id;
    private UUID productId;
    private LocalizedString productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
