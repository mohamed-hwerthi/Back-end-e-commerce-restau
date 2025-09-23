package com.foodsquad.FoodSquad.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_attributes")
@Getter
@Setter
public class ProductAttribute {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(columnDefinition = "jsonb", nullable = false)
    private LocalizedString name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttributeValue> values = new ArrayList<>() ;

}
