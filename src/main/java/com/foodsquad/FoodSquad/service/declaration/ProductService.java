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

/**
 * Service interface for managing {@link Product} entities and related operations.
 */
public interface ProductService {

    /**
     * Creates a new product.
     *
     * @param productDTO the product data transfer object
     * @return the created {@link ProductDTO}
     */
    ProductDTO createProduct(ProductDTO productDTO);

    /**
     * Retrieves a product by its ID.
     *
     * @param id the product UUID
     * @return the corresponding {@link ProductDTO}
     */
    ProductDTO getProductById(UUID id);

    /**
     * Retrieves all products with pagination and sorting options.
     *
     * @param page               the page number (0-based index)
     * @param limit              the number of items per page
     * @param sortBy             the field to sort by
     * @param desc               whether sorting should be descending
     * @param categoryId         filter by category UUID
     * @param isDefault          optional filter for default status
     * @param priceSortDirection direction of price sorting ("asc" or "desc")
     * @return a paginated response containing {@link ProductDTO} objects
     */
    PaginatedResponseDTO<ProductDTO> getAllProducts(int page, int limit, String sortBy,
                                                    boolean desc, UUID categoryId,
                                                    String isDefault, String priceSortDirection);

    /**
     * Updates an existing product.
     *
     * @param id         the product UUID
     * @param productDTO the updated product data
     * @return a {@link ResponseEntity} containing the updated {@link ProductDTO}
     */
    ResponseEntity<ProductDTO> updateProduct(UUID id, ProductDTO productDTO);

    /**
     * Deletes a product by ID.
     *
     * @param id the product UUID
     * @return a {@link ResponseEntity} containing a confirmation message
     */
    ResponseEntity<Map<String, String>> deleteProduct(UUID id);

    /**
     * Retrieves multiple products by their IDs.
     *
     * @param ids the list of product UUIDs
     * @return a {@link ResponseEntity} containing the list of {@link ProductDTO}
     */
    ResponseEntity<List<ProductDTO>> getProductsByIds(List<UUID> ids);

    /**
     * Deletes multiple products by their IDs.
     *
     * @param ids the list of product UUIDs
     * @return a {@link ResponseEntity} containing a confirmation message
     */
    ResponseEntity<Map<String, String>> deleteProductsByIds(List<UUID> ids);

    /**
     * Searches for products by category and query with pagination.
     *
     * @param filterRequest filter request DTO containing category and query
     * @param pageable      pagination details
     * @return a paginated response containing {@link ProductDTO} objects
     */
    PaginatedResponseDTO<ProductDTO> searchProductsByQuery(ProductFilterByCategoryAndQueryRequestDTO filterRequest,
                                                           Pageable pageable);

    /**
     * Finds a product by its barcode.
     *
     * @param qrCode the barcode value
     * @return the matching {@link ProductDTO}
     */
    ProductDTO findByBarCode(String qrCode);

    /**
     * Searches for products by query string with pagination.
     *
     * @param query    the search query
     * @param pageable pagination details
     * @return a paginated response containing {@link ProductDTO} objects
     */
    PaginatedResponseDTO<ProductDTO> searchProductsByQuery(String query, Pageable pageable);

    /**
     * Saves a list of products.
     *
     * @param products the list of {@link Product}
     * @return the saved list as {@link ProductDTO}
     */
    List<ProductDTO> saveProducts(List<Product> products);

    /**
     * Saves a single product entity.
     *
     * @param product the product entity
     * @return the saved {@link ProductDTO}
     */
    ProductDTO save(Product product);

    /**
     * Finds a product entity by ID.
     *
     * @param id the product UUID
     * @return the {@link Product} entity
     */
    Product findProductById(UUID id);

    /**
     * Decrements the quantity of a product.
     *
     * @param productId the product UUID
     * @param quantity  the quantity to decrement
     * @return the updated {@link ProductDTO}
     */
    ProductDTO decrementProductQuantity(UUID productId, int quantity);

    /**
     * Retrieves the discounted price of a product.
     *
     * @param productId the product UUID
     * @return the discounted {@link BigDecimal} price
     */
    BigDecimal findProductDiscountedPrice(UUID productId);

    /**
     * Finds products by promotion.
     *
     * @param promotion the {@link Promotion} entity
     * @return a list of products under the given promotion
     */
    List<Product> findByPromotion(Promotion promotion);

    /**
     * Finds products by category.
     *
     * @param category the {@link Category} entity
     * @return a list of products in the given category
     */
    List<Product> findByCategory(Category category);

    /**
     * Deletes a media file associated with a product.
     *
     * @param productId the product UUID
     * @param mediaId   the media UUID
     */
    void deleteMediaForProduct(UUID productId, UUID mediaId);

    /**
     * Retrieves all product options (products marked as options).
     *
     * @return a list of product options as {@link ProductDTO}
     */
    List<ProductDTO> getAllProductOptions();

}
