package com.foodsquad.FoodSquad.model.dto.cashier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashierSessionRequestDTO {
    private UUID cashierId;
    private Double openingBalance;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double closingBalance;
    private Double totalSales;
    private Double totalRefunds;
    private Boolean isClosed;
}
