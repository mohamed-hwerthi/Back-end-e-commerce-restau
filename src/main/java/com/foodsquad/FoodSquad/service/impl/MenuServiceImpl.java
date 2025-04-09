package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.MenuItemMapper;
import com.foodsquad.FoodSquad.mapper.MenuMapper;
import com.foodsquad.FoodSquad.model.Menu;
import com.foodsquad.FoodSquad.model.dto.MenuDTO;
import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.repository.MenuRepository;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import com.foodsquad.FoodSquad.service.declaration.MenuService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;
    private final MenuItemService menuItemService ;
    private final MenuItemMapper    menuItemMapper;



    public MenuServiceImpl(MenuRepository menuRepository, MenuMapper menuMapper, MenuItemService menuItemService, MenuItemMapper menuItemMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
        this.menuItemService = menuItemService;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuDTO getMenuById(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Menu not found  with id " + id));
        return menuMapper.toDto(menu);
    }

    @Override
    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menuMapper::toDto)
                .toList()  ;
    }

    @Override
    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = menuMapper.toEntity(menuDTO);
        if (!ObjectUtils.isEmpty(menuDTO.getMenuItemsIds())){
            menuDTO.getMenuItemsIds().forEach(
                    menuItemId-> {
                        MenuItemDTO foundedMenuItem = menuItemService.getMenuItemById(menuItemId).getBody() ;
                        MenuItem menuItem = menuItemMapper.toEntity(foundedMenuItem);
                        menu.getMenuItems().add(menuItem);
                    }
            );

        }

        Menu savedMenu = menuRepository.save(menu);
        return menuMapper.toDto(savedMenu);
    }

    @Override
    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu not found  with id  " + id);
        }
        Menu menu = menuMapper.toEntity(menuDTO);
        menu.setId(id);
        Menu updatedMenu = menuRepository.save(menu);
        return menuMapper.toDto(updatedMenu);
    }

    @Override
    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu not found  with id  " + id);
        }
        menuRepository.deleteById(id);
    }
}
