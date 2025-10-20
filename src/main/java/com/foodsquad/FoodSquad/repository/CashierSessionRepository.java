package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.CashierSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CashierSessionRepository extends JpaRepository<CashierSession, UUID> {
    List<CashierSession> findByCashierId(UUID cashierId);


    List<CashierSession> findByIsClosed(Boolean isClosed);
}
