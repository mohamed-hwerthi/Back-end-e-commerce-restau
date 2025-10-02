package com.foodsquad.FoodSquad.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name = "custom_attributes")
@Getter
@Setter
public class CustomAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Type(JsonType.class)
    @Column(name = "name", length = 2048, columnDefinition = "json", nullable = false)
    private LocalizedString name;

    @Column(nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomAttributeType type;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
