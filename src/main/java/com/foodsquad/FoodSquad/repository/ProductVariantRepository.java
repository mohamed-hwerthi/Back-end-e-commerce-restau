package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {


    boolean existsBySku(String sku);


}
