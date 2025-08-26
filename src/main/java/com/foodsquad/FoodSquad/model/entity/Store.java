package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /*
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User owner;
    */

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "email", nullable = true)
    private String email;

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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
/*
@OneToOne
    @JoinColumn(name = "cover_media_id")
    private Media coverImage;
 */



    /*
      @ManyToOne(optional = true)
    @JoinColumn(name = "activity_sector_id")
    private ActivitySector activitySector;
     */

  /*
     @OneToOne
    @JoinColumn(name = "logo_media_id")
    private Media logo;
   */

}
