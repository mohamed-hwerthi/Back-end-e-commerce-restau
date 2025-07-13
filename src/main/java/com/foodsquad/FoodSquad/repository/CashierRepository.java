package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashierRepository extends JpaRepository<Cashier, String> {

    Optional<Cashier> findByCashierId(String cashierId);

    boolean existsByCashierId(String cashierId);

}
