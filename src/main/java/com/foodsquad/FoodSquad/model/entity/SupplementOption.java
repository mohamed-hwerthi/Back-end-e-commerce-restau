package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "supplement_options")
@Getter
@Setter
public class SupplementOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotBlank(message = "Option name cannot be blank")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Min(value = 0, message = "Price must be at least 0")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplement_group_id", nullable = false)
    private SupplementGroup supplementGroup;
}
