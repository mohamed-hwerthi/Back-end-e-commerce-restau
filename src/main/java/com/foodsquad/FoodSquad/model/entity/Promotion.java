    package com.foodsquad.FoodSquad.model.entity;


    import com.foodsquad.FoodSquad.model.dto.DiscountType;
    import jakarta.persistence.DiscriminatorColumn;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Inheritance;
    import jakarta.persistence.InheritanceType;
    import jakarta.persistence.ManyToMany;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @DiscriminatorColumn(name = "promotion_type")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public  class Promotion {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        private LocalDate startDate;


        private LocalDate endDate;

        private boolean active  ;

        @ManyToMany(mappedBy = "promotions")

        private List<MenuItem> menuItems = new ArrayList<>();



    }
