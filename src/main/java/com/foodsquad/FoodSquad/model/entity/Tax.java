package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tax")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double rate;

    public Tax() {
    }

    public Tax(String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }
}

