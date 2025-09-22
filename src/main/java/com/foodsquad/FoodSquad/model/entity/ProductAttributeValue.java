package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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


    @ManyToOne
    @JoinColumn(name = "attribute_type_id")
    private AttributeType attributeType;

    private String value;

    private String displayName;

    @Column(columnDefinition = "JSON")
    private String additionalData;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private ProductAttribute productAttribute;
}
