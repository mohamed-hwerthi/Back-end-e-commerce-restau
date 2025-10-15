package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {


    @Query("""
        SELECT o FROM Order o
        WHERE (:statusCode IS NULL OR o.status.code = :statusCode)
          AND (:startDate IS NULL OR o.createdAt >= :startDate)
          AND (:endDate IS NULL OR o.createdAt <= :endDate)
        ORDER BY o.createdAt DESC
    """)
    List<Order> findOrdersByFilters(
            @Param("statusCode") String statusCode,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
