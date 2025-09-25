package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.SupplementGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupplementGroupRepository extends JpaRepository<SupplementGroup, UUID> {
}
