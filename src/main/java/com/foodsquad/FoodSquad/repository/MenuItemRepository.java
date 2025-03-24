package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.MenuItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>  {
        Page<MenuItem> findByCategory(MenuItemCategory category, Pageable pageable);
        @Query("SELECT m FROM MenuItem m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))")
        Page<MenuItem> filterByQuery(@Param("query") String query, Pageable pageable);
        Optional<MenuItem> findByBarCode(String qrCode) ;


    }