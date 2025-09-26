package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "supplement_options")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(columnDefinition = "jsonb", nullable = false)
    private LocalizedString name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplement_group_id", nullable = false)
    private SupplementGroup supplementGroup;
}
