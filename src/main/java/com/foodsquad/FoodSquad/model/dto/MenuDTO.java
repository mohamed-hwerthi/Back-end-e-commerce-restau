package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id    ;
    @NotBlank(message = "  menu  name  cannot be blank")
    private String  name  ;
    private String description  ;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long>menuItemsIds = new ArrayList<>() ;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<MenuItemDTO> menuItems = new ArrayList<>() ;

    public String getId() {

        return id;
    }

    public void setId(String id) {

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

    public List<Long> getMenuItemsIds() {

        return menuItemsIds;
    }

    public void setMenuItemsIds(List<Long> menuItemsIds) {

        this.menuItemsIds = menuItemsIds;
    }

    public List<MenuItemDTO> getMenuItems() {

        return menuItems;
    }

    public void setMenuItems(List<MenuItemDTO> menuItems) {

        this.menuItems = menuItems;
    }

}
