package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "supplement_groups")
@Getter
@Setter
public class SupplementGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotBlank(message = "Group name cannot be blank")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean obligatory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "supplementGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplementOption> options = new ArrayList<>();
}
