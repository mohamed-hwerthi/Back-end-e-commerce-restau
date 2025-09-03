package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
