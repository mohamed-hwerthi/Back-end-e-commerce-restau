package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @Column(nullable = false, name = "sub_total")
    private BigDecimal subTotal;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private OrderSource source;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "delivery_street")),
            @AttributeOverride(name = "city", column = @Column(name = "delivery_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code")),
            @AttributeOverride(name = "countryId", column = @Column(name = "delivery_country_id"))
    })
    private Address deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "cashier_session_id")
    private CashierSession cashierSession;

    @Column(name = "cash_received")
    private BigDecimal cashReceived;

    @Column(name = "change_given")
    private BigDecimal changeGiven;

    @Column(name = "is_printed")
    private Boolean isPrinted;


    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public void calculateTotal() {
        if (orderItems == null || orderItems.isEmpty()) {
            total = BigDecimal.ZERO;
        } else {
            total = orderItems.stream()
                    .map(OrderItem::calculateItemTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
        calculateTotal();
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
        calculateTotal();
    }
}
