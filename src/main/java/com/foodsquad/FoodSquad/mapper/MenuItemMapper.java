package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper( componentModel = MappingConstants.ComponentModel.SPRING , uses = {CategoryMapper.class,})

public interface MenuItemMapper {
   MenuItem toEntity(MenuItemDTO menuItemDTO);
   @Mapping(source = "categories" , target = "categoryDTOS")
   MenuItemDTO toDto(MenuItem menuItem);
   List<MenuItemDTO> toDtoList(List<MenuItem> menuItems);
}
