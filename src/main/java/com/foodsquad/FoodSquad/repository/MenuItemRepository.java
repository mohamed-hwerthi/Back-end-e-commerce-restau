package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



/*
todo : Methode are commented   until we Fix in all the transaltions
 */
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    @Query("SELECT m FROM MenuItem m JOIN m.categories c WHERE c.id = :categoryId")
    Page<MenuItem> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);



    Optional<MenuItem> findByBarCode(String qrCode);



    List<MenuItem> findAllByPromotionsContaining(Promotion promotion);

    List<MenuItem> findAllByCategoriesContaining(Category category);

    @Query(value = """
        SELECT DISTINCT m.*
        FROM menu_items m
                 LEFT JOIN menu_item_categories mc ON m.id = mc.menu_item_id
                 LEFT JOIN categories c ON mc.category_id = c.id
        WHERE (:query IS NULL 
               OR m.title->>:lang ILIKE %:query%)
          AND (:inStock IS NULL OR (:inStock = true AND m.quantity > 0) OR (:inStock = false AND m.quantity = 0))
          AND (COALESCE(:categoryIds, NULL) IS NULL OR c.id = ANY(:categoryIds))
        """, nativeQuery = true)
    Page<MenuItem> searchByQueryAndFilters(
            @Param("query") String query,
            @Param("categoryIds") UUID[] categoryIds,
            @Param("inStock") Boolean inStock,
            @Param("lang") String lang,
            Pageable pageable
    );



}