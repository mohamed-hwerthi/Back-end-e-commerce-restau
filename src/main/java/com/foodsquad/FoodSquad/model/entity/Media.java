package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "medias")
@Getter
@Setter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "path ")
    private String path;

    @Column(nullable = false, name = "url")
    private String url;

    @Column(nullable = false, name = "type")
    private String type;

    @ManyToMany(mappedBy = "medias")
    private List<MenuItem> menuItems = new ArrayList<>();

    @ManyToMany(mappedBy = "medias")
    private List<Category> categories = new ArrayList<>();


}
