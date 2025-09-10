package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String name;

    private String symbol;

    private int scale;

    public Currency(String name, String symbol, int scale) {

        this.name = name;
        this.symbol = symbol;
        this.scale = scale;
    }

}