package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "variant_attributes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"variant_id", "attribute_value_id"})
})
public class VariantAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @ManyToOne
    @JoinColumn(name = "attribute_value_id")
    private ProductAttributeValue attributeValue;
}
