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
@Table(name = "cashier_sessions")
public class CashierSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "session_number")
    private String sessionNumber;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "opening_balance")
    private Double openingBalance;

    @Column(name = "closing_balance")
    private Double closingBalance;

    @Column(name = "total_sales")
    private Double totalSales;

    @Column(name = "total_refunds")
    private Double totalRefunds;

    @Column(name = "is_closed")
    private Boolean isClosed;

    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;

    @OneToMany(mappedBy = "cashierSession", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "cashierSession", cascade = CascadeType.ALL)
    private List<CashMovement> cashMovements;
}
