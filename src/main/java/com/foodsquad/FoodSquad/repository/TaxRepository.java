package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Review;
import com.foodsquad.FoodSquad.model.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {
}
