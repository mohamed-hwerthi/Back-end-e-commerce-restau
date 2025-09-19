package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "attribute_types")
@Getter
@Setter
public class AttributeType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DataType dataType = DataType.TEXT;

    private Boolean isRequired = false;

    private Integer displayOrder = 0;

    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL)
    private List<AttributeValue> values = new ArrayList<>();

    public enum DataType {
        TEXT, NUMBER, COLOR, BOOLEAN
    }
}
