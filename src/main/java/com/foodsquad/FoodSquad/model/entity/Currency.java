package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the currency (e.g., US Dollar, Euro)
    private String symbol; // Symbol of the currency (e.g., $, €)
    private int scale; // Number of decimal places after the comma (e.g., 2 for USD)
    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();
    public Currency(String name, String symbol, int scale) {
        this.name = name;
        this.symbol = symbol;
        this.scale = scale;
    }
}