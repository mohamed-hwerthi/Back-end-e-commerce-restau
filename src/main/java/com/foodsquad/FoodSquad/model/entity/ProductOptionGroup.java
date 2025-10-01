    package com.foodsquad.FoodSquad.model.entity;

    import com.vladmihalcea.hibernate.type.json.JsonType;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.Type;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;

    @Entity
    @Table(name = "product_option_groups")
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class ProductOptionGroup {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(name = "id", nullable = false, updatable = false)
        private UUID id;

        @Type(JsonType.class)
        @Column(name = "name", columnDefinition = "json", nullable = false)
        private LocalizedString name;

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @OneToMany(mappedBy = "productOptionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ProductOption> productOptions = new ArrayList<>();
    }
