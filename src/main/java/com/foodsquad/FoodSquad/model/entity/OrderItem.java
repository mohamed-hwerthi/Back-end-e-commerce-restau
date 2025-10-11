package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, name = "unit_price")
    private BigDecimal unitPrice;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemOption> options = new ArrayList<>();

    public BigDecimal calculateItemTotal() {
        BigDecimal baseTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal optionsTotal = options.stream()
                .map(OrderItemOption::getOptionTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return baseTotal.add(optionsTotal);
    }

    public void addOption(OrderItemOption option) {
        options.add(option);
        option.setOrderItem(this);
    }

    public void removeOption(OrderItemOption option) {
        options.remove(option);
        option.setOrderItem(null);
    }
}
