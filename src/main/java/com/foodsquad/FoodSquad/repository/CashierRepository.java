package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CashierRepository extends JpaRepository<Cashier, UUID> {



}
