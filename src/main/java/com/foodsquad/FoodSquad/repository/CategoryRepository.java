package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category , Long> {
   Optional<Category> findByName(String name) ;
   List<Category> findAllByPromotionsContaining(Promotion promotion);


}
