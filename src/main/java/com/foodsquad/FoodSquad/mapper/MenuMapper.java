package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.MenuDTO;
import com.foodsquad.FoodSquad.model.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface MenuMapper {
    @Mapping(source = "products", target = "products")
    MenuDTO toDto(Menu menu);

    Menu toEntity(MenuDTO menuDTO);

}
