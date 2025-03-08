package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.Menu;
import com.foodsquad.FoodSquad.model.dto.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING   , uses = {MenuItemMapper.class})
public interface  MenuMapper {
    @Mapping(source = "menuItems", target = "menuItems")
    MenuDTO toDto(Menu menu);

    Menu toEntity(MenuDTO menuDTO);

}
