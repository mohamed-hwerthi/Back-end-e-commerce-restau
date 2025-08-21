package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Timbre;
import jakarta.validation.constraints.DecimalMax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimbreRepository  extends JpaRepository<Timbre , String> {


}
