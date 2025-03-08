package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;


@Table(name = "category")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long  id   ;
    @Column(nullable = false , name = "name")
    private String name ;
    @Column(nullable = false , name = "description")
    private String description  ;
    @ManyToMany(mappedBy = "categories")
    private List<MenuItem> menuItems = new ArrayList<>();

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public List<MenuItem> getMenuItems() {

        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {

        this.menuItems = menuItems;
    }

}
