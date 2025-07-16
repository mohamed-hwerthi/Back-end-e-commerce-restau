package com.foodsquad.FoodSquad.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "promotion_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "startDate")
    private LocalDate startDate;

    @Column(nullable = true, name = "endDate")
    private LocalDate endDate;

    @Column(nullable = true, name = "active")
    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;


    private PromotionTarget promotionTarget;

    @ManyToMany(mappedBy = "promotions")
    private List<MenuItem> menuItems = new ArrayList<>();

    @ManyToMany(mappedBy = "promotions")
    private List<Category> categories = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
    }



}
