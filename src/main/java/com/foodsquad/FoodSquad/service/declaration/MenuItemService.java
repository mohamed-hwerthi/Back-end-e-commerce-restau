package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.MenuItemFilterByCategoryAndQueryRequestDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MenuItemService {

    ResponseEntity<MenuItemDTO> createMenuItem(MenuItemDTO menuItemDTO);

    MenuItemDTO getMenuItemById(Long id);

    PaginatedResponseDTO<MenuItemDTO> getAllMenuItems(int page, int limit, String sortBy, boolean desc, Long catgoryId, String isDefault, String priceSortDirection );

    ResponseEntity<MenuItemDTO> updateMenuItem(Long id, MenuItemDTO menuItemDTO);

    ResponseEntity<Map<String, String>> deleteMenuItem(Long id);

    ResponseEntity<List<MenuItemDTO>> getMenuItemsByIds(List<Long> ids);

    ResponseEntity<Map<String, String>> deleteMenuItemsByIds(List<Long> ids);

    PaginatedResponseDTO<MenuItemDTO> searchMenuItemsByQuery(MenuItemFilterByCategoryAndQueryRequestDTO menuItemFilterByCategoryAndQueryRequestDTO, Pageable pageable);

    MenuItemDTO findByBarCode(String qrCode);

    PaginatedResponseDTO<MenuItemDTO> searchMenuItemsByQuery(String query, Pageable pageable);

    List<MenuItemDTO>saveMenuItems(List<MenuItem> menuItems);

    MenuItemDTO save(MenuItem  menuItem) ;

    MenuItem   findMenuItemById(Long id) ;







}
