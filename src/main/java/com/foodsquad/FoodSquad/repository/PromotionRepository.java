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
                LEFT JOIN p.products mi
                LEFT JOIN p.categories c
                LEFT JOIN c.products cmi
                WHERE mi.id = :ProductId OR cmi.id = :ProductId
            """)
    List<Promotion> findAllPromotionsForProduct(@Param("ProductId") UUID ProductId);


}
