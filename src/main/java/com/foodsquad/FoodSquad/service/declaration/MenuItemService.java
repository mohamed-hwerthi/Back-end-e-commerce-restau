package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.MenuItemFilterByCategoryAndQueryRequestDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MenuItemService {

    MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO);

    MenuItemDTO getMenuItemById(UUID id);

    PaginatedResponseDTO<MenuItemDTO> getAllMenuItems(int page, int limit, String sortBy, boolean desc, UUID catgoryId, String isDefault, String priceSortDirection);

    ResponseEntity<MenuItemDTO> updateMenuItem(UUID id, MenuItemDTO menuItemDTO);

    ResponseEntity<Map<String, String>> deleteMenuItem(UUID id);

    ResponseEntity<List<MenuItemDTO>> getMenuItemsByIds(List<UUID> ids);

    ResponseEntity<Map<String, String>> deleteMenuItemsByIds(List<UUID> ids);

    PaginatedResponseDTO<MenuItemDTO> searchMenuItemsByQuery(MenuItemFilterByCategoryAndQueryRequestDTO menuItemFilterByCategoryAndQueryRequestDTO, Pageable pageable);

    MenuItemDTO findByBarCode(String qrCode);

    PaginatedResponseDTO<MenuItemDTO> searchMenuItemsByQuery(String query, Pageable pageable);

    List<MenuItemDTO> saveMenuItems(List<MenuItem> menuItems);

    MenuItemDTO save(MenuItem menuItem);

    MenuItem findMenuItemById(UUID id);

    MenuItemDTO decrementMenuItemQuantity(UUID menuItemId, int quantity);

    BigDecimal findMenuItemDiscountedPrice(UUID menuItemId);

    List<MenuItem> findByPromotion(Promotion promotion);

    List<MenuItem> findByCategory(Category category);

    void deleteMediaForMenuItem(UUID menuItemId, UUID mediaId);


}
