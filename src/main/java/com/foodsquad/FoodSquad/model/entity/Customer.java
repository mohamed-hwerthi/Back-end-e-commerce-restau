package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer  extends  User{
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

}
