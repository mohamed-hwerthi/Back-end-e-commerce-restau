package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false)
    private Double totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private Boolean paid = false;

    @ElementCollection
    @CollectionTable(name = "order_menu_item", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "menu_item_id")
    @Column(name = "quantity")
    private Map<MenuItem, Integer> menuItemsWithQuantity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;






}
