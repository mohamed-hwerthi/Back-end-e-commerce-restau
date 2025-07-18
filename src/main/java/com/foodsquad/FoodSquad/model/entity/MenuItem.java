    package com.foodsquad.FoodSquad.model.entity;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.Positive;
    import lombok.Data;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Table(name = "menu_items")
    @Data
    public class MenuItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String title;

        @Column(nullable = false)
        private String description;


        @Column(nullable = false)
        private Double price = 1.0;

        @Column(nullable = true, name = "codeBar", unique = true)
        private String barCode;

       @Column (nullable = false  , name = "purchase_price")
       @Positive(message = "Purchase price must be positive")
        private BigDecimal purchasePrice ;

       @Column(nullable = false , name = "quantity" )
       @Min(value = 0, message = "Quantity must be at least 0")
       private int quantity;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;


        @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Review> reviews = new ArrayList<>();

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdOn;

        @ManyToMany(mappedBy = "menuItems")
        private List<Menu> menus;
        @ManyToOne
        @JoinColumn(name = "currency_id", nullable = false)
        private Currency currency;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "tax_id", referencedColumnName = "id")
        private Tax tax;
            @ManyToMany
            @JoinTable(
                    name = "menu_item_categories",
                    joinColumns = @JoinColumn(name = "menu_item_id"),

                    inverseJoinColumns = @JoinColumn(name = "category_id")
            )
        private List<Category> categories = new ArrayList<>();

        @ManyToMany
        @JoinTable(
                name = "x",
                joinColumns = @JoinColumn(name = "menu_item_id"),
                inverseJoinColumns = @JoinColumn(name = "media_id")
        )
        private List<Media> medias = new ArrayList<>();


        @ManyToMany
        @JoinTable(
                name = "menu_item_promotions",
                joinColumns = @JoinColumn(name = "menu_item_id"),
                inverseJoinColumns = @JoinColumn(name = "promotion_id")
        )
        private List<Promotion> promotions = new ArrayList<>();


        @PrePersist
        protected void onCreate() {

            this.createdOn = LocalDateTime.now();
        }

        @ManyToOne
        @JoinColumn(name = "store_id")
        private Store store;


    }
