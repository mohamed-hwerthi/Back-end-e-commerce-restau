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

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_sector_id")
    private ActivitySector activitySector;


    private String phone;
    private String email;
    private String address;


    @Column(length = 2048)
    private String about;


    @OneToOne
    @JoinColumn(name = "logo_media_id")
    private Media logo;

    @OneToOne
    @JoinColumn(name = "cover_media_id")
    private Media coverImage;


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
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
