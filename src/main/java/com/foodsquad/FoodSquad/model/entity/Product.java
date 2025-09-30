package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(columnDefinition = "jsonb", nullable = false)
    private LocalizedString title;

    @Column(columnDefinition = "jsonb", nullable = true)
    private LocalizedString description;


    @Column(nullable = false)
    @Min(value = 0, message = "Quantity must be at least 0")
    private BigDecimal price;

    @Column(nullable = true, name = "code_bar", unique = true)
    private String barCode;

    @Column(nullable = true, name = "sku", unique = true)
    private String sku;

    @Column(nullable = false, name = "purchase_price")
    @Positive(message = "Purchase price must be positive")
    private BigDecimal purchasePrice;
    @Column(nullable = true, name = "low_stock_threshold")
    @Min(value = 0, message = "Low stock threshold must be at least 0")
    private int lowStockThreshold;

    @Column(nullable = false, name = "quantity")
    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantity;

    @Column(nullable = false , name = "is_variant")
    private boolean isVariant = false;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
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
            name = " product_medias",
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
    private List<SupplementGroup> supplementGroups = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomAttribute> customAttributes = new ArrayList<>();

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
