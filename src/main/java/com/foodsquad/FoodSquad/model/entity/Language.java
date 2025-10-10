package com.foodsquad.FoodSquad.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name = "languages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "code", length = 5, nullable = false, unique = true)
    private String code;

    @Type(JsonType.class)
    @Column(name = "name", columnDefinition = "json", nullable = false)
    private LocalizedString name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
}
