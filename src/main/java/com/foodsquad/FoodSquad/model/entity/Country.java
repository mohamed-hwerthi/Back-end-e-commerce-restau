package com.foodsquad.FoodSquad.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name = "countries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @Column(name = "id", updatable = false)
    private String id;

    @Column(name = "code", length = 2, nullable = false, unique = true)
    private String code;

    @Type(JsonType.class)
    @Column(name = "name", columnDefinition = "json", nullable = false)
    private LocalizedString name;

    @Column(name = "flag_url")
    private String flagUrl;


}
