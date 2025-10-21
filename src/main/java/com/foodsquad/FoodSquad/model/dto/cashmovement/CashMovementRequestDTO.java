package com.foodsquad.FoodSquad.model.dto.cashmovement;

import com.foodsquad.FoodSquad.model.entity.MovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashMovementRequestDTO {
    @NotNull(message = "Movement type is required")
    private MovementType type;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotBlank(message = "Reason is required")
    private String reason;
    
    @NotNull(message = "Cashier session ID is required")
    private UUID cashierSessionId;
    
    @NotNull(message = "Cashier ID is required")
    private UUID cashierId;
}
