package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository  extends JpaRepository<Menu , Long> {

}
