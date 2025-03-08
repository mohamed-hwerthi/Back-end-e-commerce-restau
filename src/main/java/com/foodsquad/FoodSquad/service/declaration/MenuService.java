package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MenuDTO;

import java.util.List;

public interface MenuService {
    MenuDTO getMenuById(Long id);
    List<MenuDTO> getAllMenus();
    MenuDTO createMenu(MenuDTO menuDTO);
    MenuDTO updateMenu(Long id, MenuDTO menuDTO);
    void deleteMenu(Long id);

}
