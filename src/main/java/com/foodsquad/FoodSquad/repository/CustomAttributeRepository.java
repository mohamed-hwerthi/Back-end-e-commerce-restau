package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.CustomAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomAttributeRepository extends JpaRepository<CustomAttribute, UUID> {
}
