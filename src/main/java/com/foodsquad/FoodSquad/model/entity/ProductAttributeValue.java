package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "attribute_values")
@Getter
@Setter
public class ProductAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

   @Column(name = "value" , nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "attribute_id" , nullable = false)
    private ProductAttribute productAttribute;

    @ManyToMany(mappedBy = "variantAttributes")
    private Set<Product> products = new HashSet<>();
}
