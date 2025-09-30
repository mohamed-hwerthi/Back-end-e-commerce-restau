package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "taxes")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String name;
    private Double rate;

    public Tax() {
    }

    public Tax(String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }
}

