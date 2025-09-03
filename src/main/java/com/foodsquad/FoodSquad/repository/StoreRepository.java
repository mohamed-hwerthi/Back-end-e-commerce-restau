package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Store;
import com.foodsquad.FoodSquad.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    boolean existsByName(String name);

      Optional < Store> findByOwner(User owner);
}
    