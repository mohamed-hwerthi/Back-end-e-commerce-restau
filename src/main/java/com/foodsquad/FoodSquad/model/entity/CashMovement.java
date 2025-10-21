package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    private BigDecimal amount;
    private String reason;
    private LocalDateTime timestamp;

    @ManyToOne
    private CashierSession cashierSession;

    @ManyToOne
    private User cashier;
}
