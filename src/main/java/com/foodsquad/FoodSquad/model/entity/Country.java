package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Country {
    @Id
    @Column(name = "code", length = 2, nullable = false, unique = true)
    private String code;

    @Type(type = "json")
    @Column(name = "name", columnDefinition = "json")
    private LocalizedString name;

    @Column(name = "flag_url")
    private String flagUrl;
}
