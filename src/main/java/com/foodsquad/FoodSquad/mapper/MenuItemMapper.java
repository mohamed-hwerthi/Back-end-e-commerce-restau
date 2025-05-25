package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.TaxDTO;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, MediaMapper.class, TaxMapper.class})

public interface MenuItemMapper {

    MenuItem toEntity(MenuItemDTO menuItemDTO);

    MenuItemDTO toDto(MenuItem menuItem);

    List<MenuItemDTO> toDtoList(List<MenuItem> menuItems);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateMenuItemFromDto(MenuItemDTO dto, @MappingTarget MenuItem entity);

    default MenuItemDTO toMenuItemDtoWithMoreInformation(MenuItem menuItem, int salesCount, long reviewCount, double averageRating) {

        MenuItemDTO menuItemDTO = toDto(menuItem);
        menuItemDTO.setSalesCount(salesCount);
        menuItemDTO.setReviewCount(reviewCount);
        menuItemDTO.setAverageRating(averageRating);


        return menuItemDTO;
    }

}
