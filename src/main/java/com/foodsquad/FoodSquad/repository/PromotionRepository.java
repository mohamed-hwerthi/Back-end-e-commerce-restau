package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    @Query("""
        SELECT DISTINCT p FROM Promotion p
        LEFT JOIN p.menuItems mi
        LEFT JOIN p.categories c
        LEFT JOIN c.menuItems cmi
        WHERE mi.id = :menuItemId OR cmi.id = :menuItemId
    """)
    List<Promotion> findAllPromotionsForMenuItem(@Param("menuItemId") UUID menuItemId);




}
