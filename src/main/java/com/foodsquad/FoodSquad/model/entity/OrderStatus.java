    package com.foodsquad.FoodSquad.model.entity;


    import com.vladmihalcea.hibernate.type.json.JsonType;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.Type;

    import java.util.UUID;

    @Entity
    @Table(name = "order_statuses")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class OrderStatus {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        private String code;

        @Type(JsonType.class)
        @Column(name = "name", columnDefinition = "json")
        private LocalizedString name;
    }
