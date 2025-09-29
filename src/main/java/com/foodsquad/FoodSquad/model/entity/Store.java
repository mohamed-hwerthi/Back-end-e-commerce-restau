package com.foodsquad.FoodSquad.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

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


    @Type(JsonType.class)
    @Column(name = "about", length = 2048 , columnDefinition = "json", nullable = false)
    private LocalizedString about;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "address", nullable = true)
    private String address;

    private String city  ;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "facebook_url", nullable = true)
    private String facebookUrl;

    @Column(name = "email_contact" , nullable = true)
    private String  emailContact ;

    @Column(name = "whatsapp"  ,  nullable = false)
    private String whatsapp ;


    @Column(name = "instagram_url", nullable = true)
    private String instagramUrl;

    @Column(name = "linked_in_url", nullable = true)
    private String linkedInUrl;

    @Column(name = "website_url", nullable = true)
    private String websiteUrl;

    @Column(name = "postal_code" , nullable =true)
    private String postalCode  ;



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
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(optional = true)
    @JoinColumn(name = "default_language_id")
    private Language defaultLanguage;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


}
