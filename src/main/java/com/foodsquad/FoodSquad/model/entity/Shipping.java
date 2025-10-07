package com.foodsquad.FoodSquad.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "shipping")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private String shippingMethod;

    private double shippingCost;
}

