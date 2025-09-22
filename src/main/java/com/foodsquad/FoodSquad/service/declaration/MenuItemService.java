package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MenuItemFilterByCategoryAndQueryRequestDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MenuItemService {

    ProductDTO createMenuItem(ProductDTO productDTO);

    ProductDTO getMenuItemById(UUID id);

    PaginatedResponseDTO<ProductDTO> getAllMenuItems(int page, int limit, String sortBy, boolean desc, UUID catgoryId, String isDefault, String priceSortDirection);

    ResponseEntity<ProductDTO> updateMenuItem(UUID id, ProductDTO productDTO);

    ResponseEntity<Map<String, String>> deleteMenuItem(UUID id);

    ResponseEntity<List<ProductDTO>> getMenuItemsByIds(List<UUID> ids);

    ResponseEntity<Map<String, String>> deleteMenuItemsByIds(List<UUID> ids);

    PaginatedResponseDTO<ProductDTO> searchMenuItemsByQuery(MenuItemFilterByCategoryAndQueryRequestDTO menuItemFilterByCategoryAndQueryRequestDTO, Pageable pageable);

    ProductDTO findByBarCode(String qrCode);

    PaginatedResponseDTO<ProductDTO> searchMenuItemsByQuery(String query, Pageable pageable);

    List<ProductDTO> saveMenuItems(List<Product> products);

    ProductDTO save(Product product);

    Product findMenuItemById(UUID id);

    ProductDTO decrementMenuItemQuantity(UUID menuItemId, int quantity);

    BigDecimal findMenuItemDiscountedPrice(UUID menuItemId);

    List<Product> findByPromotion(Promotion promotion);

    List<Product> findByCategory(Category category);

    void deleteMediaForMenuItem(UUID menuItemId, UUID mediaId);


}
