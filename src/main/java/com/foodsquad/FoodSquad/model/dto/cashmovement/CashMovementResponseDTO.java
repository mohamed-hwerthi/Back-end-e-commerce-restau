package com.foodsquad.FoodSquad.model.dto.cashmovement;

import com.foodsquad.FoodSquad.model.entity.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashMovementResponseDTO {
    private UUID id;
    private MovementType type;
    private BigDecimal amount;
    private String reason;
    private LocalDateTime timestamp;
    private UUID cashierSessionId;
    private UUID cashierId;
    private String cashierName;
}
