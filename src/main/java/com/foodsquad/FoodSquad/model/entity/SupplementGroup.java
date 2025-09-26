package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "supplement_groups")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(columnDefinition = "jsonb", nullable = false)
    private LocalizedString name;

    @Column(nullable = false)
    private boolean obligatory;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "supplementGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplementOption> supplementOptions = new ArrayList<>();
}
