package com.foodsquad.FoodSquad.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "path ")
    private String path;

    @Column(nullable = false, name = "url")
    private String url;

    @Column(nullable = false, name = "type")
    private String type;


    @ManyToMany(mappedBy = "medias")
    private List<MenuItem> menuItems = new ArrayList<>();
    @ManyToMany(mappedBy = "medias")
    private List<Category>categories  = new ArrayList<>();


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

    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public List<MenuItem> getMenuItems() {

        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {

        this.menuItems = menuItems;
    }

    public List<Category> getCategories() {

        return categories;
    }

    public void setCategories(List<Category> categories) {

        this.categories = categories;
    }

}
