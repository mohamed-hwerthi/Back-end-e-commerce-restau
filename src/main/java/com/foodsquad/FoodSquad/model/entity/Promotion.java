package com.foodsquad.FoodSquad.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "promotion_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "startDate")
    private LocalDate startDate;

    @Column(nullable = true, name = "endDate")
    private LocalDate endDate;

    @Column(nullable = true, name = "active")
    private boolean active;

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    private PromotionTarget promotionTarget;

    @ManyToMany(mappedBy = "promotions")
    private List<Product> products = new ArrayList<>();

    @ManyToMany(mappedBy = "promotions")
    private List<Category> categories = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
