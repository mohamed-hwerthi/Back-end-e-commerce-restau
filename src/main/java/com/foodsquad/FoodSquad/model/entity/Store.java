package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
        private String slug;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "facebook_url", nullable = true)
    private String facebookUrl;



    @Column(name = "instagram_url", nullable = true)
    private String instagramUrl;

    @Column(name = "linked_in_url", nullable = true)
    private String linkedInUrl;

    @Column(name = "website_url", nullable = true)
    private String websiteUrl;

    @Column(name = "about", length = 2048, nullable = true)
    private String about;
    
    @Column(name = "background_color", length = 10, nullable = true)
    private String backgroundColor;

    @Column(name = "template_name", nullable = true)
    private String templateName;

    @Column(name = "accent_color", length = 10, nullable = true)
    private String accentColor;



    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = true)
    @JoinColumn(name = "activity_sector_id")
    private ActivitySector activitySector;


    @OneToOne
    @JoinColumn(name = "logo_media_id")
    private Media logo;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Currency currency  ;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}
