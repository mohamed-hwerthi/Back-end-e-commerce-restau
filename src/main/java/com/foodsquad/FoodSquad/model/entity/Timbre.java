package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "Timbre")
@Data
@Entity
public class Timbre {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Double  amount ;

}
