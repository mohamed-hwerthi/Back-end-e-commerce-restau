package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.MenuItemCategory;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Query("SELECT m FROM MenuItem m JOIN m.categories c WHERE c.id = :categoryId")
    Page<MenuItem> findByCategoryId(@Param("categoryId") Long categoryId , Pageable pageable);

    @Query("SELECT DISTINCT m FROM MenuItem m JOIN m.categories c " +
            "WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "AND c.id IN :categoryIds")
    Page<MenuItem> filterByQueryAndCategories(
            @Param("query") String query,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );
    Optional<MenuItem> findByBarCode(String qrCode);
    @Query("SELECT m FROM MenuItem m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<MenuItem> findByQuery(
            @Param("query") String query,
            Pageable pageable
    );

    List<MenuItem> findAllByPromotionsContaining(Promotion promotion);

    List<MenuItem> findAllByCategoriesContaining(Category category);





}