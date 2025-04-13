package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Timbre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimbreRepository  extends JpaRepository<Timbre , String> {
}
