package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "Product_options")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false  ,  name="override_price")
    private BigDecimal overridePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_group_id", nullable = false)
    private ProductOptionGroup  productOptionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_product_id", nullable = false)
    private Product linkedProduct;
}
