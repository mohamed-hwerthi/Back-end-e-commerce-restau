package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {


    @Query(value = """
            select * from orders o
            join order_statuses s on s.id = o.status_id
            where (:statusCode is null or s.code = cast(:statusCode as varchar))
              and (:startDate is null or o.created_at >= cast(:startDate as timestamp))
              and (:endDate is null or o.created_at <= cast(:endDate as timestamp))
              and (:source is null or o.source = cast(:source as varchar))
            order by o.created_at desc
            """, nativeQuery = true)
    Page<Order> findOrdersByFilters(
            @Param("statusCode") String statusCode,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("source") String source,
            Pageable pageable);

    /**
     * Finds all orders for a given source, sorted by createdAt descending.
     */
    Page<Order> findAllBySourceOrderByCreatedAtDesc(String source, Pageable pageable);
    
    /**
     * Finds all orders for a specific customer, sorted by creation date descending.
     *
     * @param customerId ID of the customer
     * @param pageable  Pagination information
     * @return Page of orders for the customer
     */
    Page<Order> findByCustomerIdOrderByCreatedAtDesc(UUID customerId, Pageable pageable);
    
    /**
     * Finds all orders for a specific cashier session, sorted by creation date descending.
     *
     * @param sessionId ID of the cashier session
     * @param pageable Pagination information
     * @return Page of orders for the cashier session
     */
    Page<Order> findByCashierSessionIdOrderByCreatedAtDesc(UUID sessionId, Pageable pageable);
}
