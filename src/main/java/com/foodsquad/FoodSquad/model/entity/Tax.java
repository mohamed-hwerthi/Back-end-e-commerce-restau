package com.foodsquad.FoodSquad.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "tax")

    private MenuItem menuItem;

    public Tax() {}

    public Tax(String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }
}

