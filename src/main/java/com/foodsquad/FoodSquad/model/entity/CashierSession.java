package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashierSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    private String sessionNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double openingBalance;
    private Double closingBalance;
    private Double totalSales;
    private Double totalRefunds;
    private Boolean isClosed;

    @ManyToOne
    private User cashier;

    @ManyToOne
    private Store store;

    @OneToMany(mappedBy = "cashierSession", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "cashierSession", cascade = CascadeType.ALL)
    private List<CashMovement> cashMovements;
}
