package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {

}
