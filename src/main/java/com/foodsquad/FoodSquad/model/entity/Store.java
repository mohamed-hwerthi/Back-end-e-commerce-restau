package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;


    private String phone;

    private String email;

    private String address;


    @Column(length = 2048)
    private String about;


    private String facebookUrl;

    private String instagramUrl;

    private String linkedInUrl;

    private String websiteUrl;


    @Column(length = 10)
    private String backgroundColor;

    private String templateName;

    @Column(length = 10)
    private String accentColor;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;


    @PrePersist
    protected void onCreate() {

        this.createdOn = LocalDateTime.now();
    }

}
