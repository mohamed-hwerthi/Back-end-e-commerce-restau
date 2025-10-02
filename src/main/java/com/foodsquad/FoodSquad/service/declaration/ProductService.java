package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.ProductFilterByCategoryAndQueryRequestDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(UUID id);

    PaginatedResponseDTO<ProductDTO> getAllProducts(int page, int limit, String sortBy, boolean desc, UUID catgoryId, String isDefault, String priceSortDirection);

    ResponseEntity<ProductDTO> updateProduct(UUID id, ProductDTO productDTO);

    ResponseEntity<Map<String, String>> deleteProduct(UUID id);

    ResponseEntity<List<ProductDTO>> getProductsByIds(List<UUID> ids);

    ResponseEntity<Map<String, String>> deleteProductsByIds(List<UUID> ids);

    PaginatedResponseDTO<ProductDTO> searchProductsByQuery(ProductFilterByCategoryAndQueryRequestDTO ProductFilterByCategoryAndQueryRequestDTO, Pageable pageable);

    ProductDTO findByBarCode(String qrCode);

    PaginatedResponseDTO<ProductDTO> searchProductsByQuery(String query, Pageable pageable);

    List<ProductDTO> saveProducts(List<Product> products);

    ProductDTO save(Product product);

    Product findProductById(UUID id);

    ProductDTO decrementProductQuantity(UUID ProductId, int quantity);

    BigDecimal findProductDiscountedPrice(UUID ProductId);

    List<Product> findByPromotion(Promotion promotion);

    List<Product> findByCategory(Category category);

    void deleteMediaForProduct(UUID ProductId, UUID mediaId);

    List<ProductDTO> getAllProductOptions();


}
