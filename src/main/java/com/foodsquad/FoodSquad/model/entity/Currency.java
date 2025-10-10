package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currencies")
public class Currency {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false, unique = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private LocalizedString name;

    private String symbol;

    private int scale;

    public Currency(LocalizedString name, String symbol, int scale) {

        this.name = name;
        this.symbol = symbol;
        this.scale = scale;
    }

}