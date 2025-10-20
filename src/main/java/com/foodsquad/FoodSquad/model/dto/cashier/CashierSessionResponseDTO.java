package com.foodsquad.FoodSquad.model.dto.cashier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashierSessionResponseDTO {
    private UUID id;
    private String sessionNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double openingBalance;
    private Double closingBalance;
    private Double totalSales;
    private Double totalRefunds;
    private Boolean isClosed;
    private UUID cashierId;
    private String cashierName;
    private List<UUID> orderIds;
    private List<UUID> cashMovementIds;
}
