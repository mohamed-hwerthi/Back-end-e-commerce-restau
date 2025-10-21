package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.CashMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CashMovementRepository extends JpaRepository<CashMovement, UUID> {
    List<CashMovement> findByCashierSessionId(UUID cashierSessionId);

    List<CashMovement> findByCashierId(UUID cashierId);
}
