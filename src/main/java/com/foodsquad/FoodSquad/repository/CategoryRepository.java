package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
 //   Optional<Category> findByName(String name);

    List<Category> findAllByPromotionsContaining(Promotion promotion);
    /*

     todo   :we have to return to this methode after the changed has beeen unded
     */

//    Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
//            String name, String description, Pageable pageable
//    );
}
