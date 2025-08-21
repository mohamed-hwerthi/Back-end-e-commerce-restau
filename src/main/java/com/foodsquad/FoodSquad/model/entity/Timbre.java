package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Table(name = "Timbre")
@Data
@Entity
@FilterDef(name = "storeFilter", parameters = @ParamDef(name = "storeId", type = String.class))
@Filter(name = "storeFilter", condition = "store_id = :storeId")
public class Timbre {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Double  amount ;


}
