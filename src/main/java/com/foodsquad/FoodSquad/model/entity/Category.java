package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Table(name = "category")
@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, name = "description")
    private String description;
    @ManyToMany(mappedBy = "categories")
    private List<MenuItem> menuItems = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "categories_medias",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private List<Media> medias = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "category_promotions",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private List<Promotion> promotions = new ArrayList<>();


}
