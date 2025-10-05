package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT m FROM Product m JOIN m.categories c WHERE c.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);


    Optional<Product> findByBarCode(String qrCode);


    List<Product> findAllByPromotionsContaining(Promotion promotion);

    List<Product> findByIsOptionTrue();

    List<Product> findAllByCategoriesContaining(Category category);

    @Query(value = """
            SELECT DISTINCT p.*
            FROM products p
                     LEFT JOIN product_categories pc ON p.id = pc.product_id
                     LEFT JOIN categories c ON pc.category_id = c.id
            WHERE (:query IS NULL 
                   OR p.title->>:lang ILIKE %:query%)
              AND (:inStock IS NULL OR (:inStock = true AND p.quantity > 0) OR (:inStock = false AND p.quantity = 0))
              AND (COALESCE(:categoryIds, NULL) IS NULL OR c.id = ANY(:categoryIds))
              AND p.is_variant = false
            """, nativeQuery = true)
    Page<Product> searchByQueryAndFilters(
            @Param("query") String query,
            @Param("categoryIds") UUID[] categoryIds,
            @Param("inStock") Boolean inStock,
            @Param("lang") String lang,
            Pageable pageable
    );

    /**
     * Search products by language-aware JSON fields (title & description).
     */
    @Query(
            value = """
            SELECT DISTINCT p.*
            FROM products p
            LEFT JOIN product_categories pc ON p.id = pc.product_id
            LEFT JOIN categories c ON pc.category_id = c.id
            WHERE (
                LOWER(p.title ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(p.description ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            """,
            countQuery = """
            SELECT COUNT(DISTINCT p.id)
            FROM products p
            LEFT JOIN product_categories pc ON p.id = pc.product_id
            LEFT JOIN categories c ON pc.category_id = c.id
            WHERE (
                LOWER(p.title ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(p.description ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            """,
            nativeQuery = true
    )
    Page<Product> searchByJsonLanguage(
            @Param("query") String query,
            @Param("lang") String lang,
            Pageable pageable
    );


    /**
     * Same as above, but with category filter.
     */
    @Query(
            value = """
            SELECT DISTINCT p.*
            FROM products p
            LEFT JOIN product_categories pc ON p.id = pc.product_id
            LEFT JOIN categories c ON pc.category_id = c.id
            WHERE (
                LOWER(p.title ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(p.description ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            AND c.id = :categoryId
            """,
            countQuery = """
            SELECT COUNT(DISTINCT p.id)
            FROM products p
            LEFT JOIN product_categories pc ON p.id = pc.product_id
            LEFT JOIN categories c ON pc.category_id = c.id
            WHERE (
                LOWER(p.title ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(p.description ->> :lang) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            AND c.id = :categoryId
            """,
            nativeQuery = true
    )
    Page<Product> searchByJsonLanguageAndCategory(
            @Param("query") String query,
            @Param("lang") String lang,
            @Param("categoryId") UUID categoryId,
            Pageable pageable
    );







}