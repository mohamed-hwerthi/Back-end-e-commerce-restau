package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "countries")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @Column(name = "code", length = 2, nullable = false, unique = true)
    private String code;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "flag_url")
    private String flagUrl;
}
