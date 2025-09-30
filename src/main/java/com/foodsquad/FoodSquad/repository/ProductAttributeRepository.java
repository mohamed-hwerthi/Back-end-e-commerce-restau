package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, UUID> {
}
