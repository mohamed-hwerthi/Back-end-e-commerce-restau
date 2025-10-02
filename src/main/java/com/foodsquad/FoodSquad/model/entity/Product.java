package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(columnDefinition = "jsonb", nullable = false)
    private LocalizedString title;

    @Column(columnDefinition = "jsonb")
    private LocalizedString description;

    @Column(nullable = false)
    @PositiveOrZero(message = "Price must be zero or positive")
    private BigDecimal price;

    @Column(name = "code_bar", unique = true)
    private String barCode;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "purchase_price", nullable = false)
    @Positive(message = "Purchase price must be greater than zero")
    private BigDecimal purchasePrice;

    @Column(name = "low_stock_threshold")
    @PositiveOrZero(message = "Low stock threshold must be zero or positive")
    private int lowStockThreshold;

    @Column(name = "quantity", nullable = false)
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private int quantity;

    @Column(name = "is_variant", nullable = false)
    private boolean isVariant = false;
    

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tax_id", referencedColumnName = "id")
    private Tax tax;

    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_medias",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private List<Media> medias = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_promotions",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private List<Promotion> promotions = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductAttribute> attributes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Product parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> variants = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionGroup> productOptionGroups = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomAttribute> customAttributes = new ArrayList<>();

    @Column(name = "is_option", nullable = false)
    private boolean isOption = false;

    @ManyToMany
    @JoinTable(
            name = "product_variant_attributes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    private Set<ProductAttributeValue> variantAttributes = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

