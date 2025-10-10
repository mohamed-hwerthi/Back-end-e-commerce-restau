package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "activity_sectors")
public class ActivitySector {

    @Id
    @Column(name = "id", updatable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "jsonb", nullable = false)
    private LocalizedString name;

}