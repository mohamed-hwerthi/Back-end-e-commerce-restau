package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Category;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
 //   Optional<Category> findByName(String name);

    List<Category> findAllByPromotionsContaining(Promotion promotion);
    /*

     todo   :we have to return to this methode after the changed has beeen unded
     */

    @Query(value = """
            SELECT * FROM categories c
            WHERE (:searchTerm IS NULL
                   OR c.name->>:lang ILIKE %:searchTerm%
                   OR c.description->>:lang ILIKE %:searchTerm%)
            """, nativeQuery = true)
    Page<Category> searchByLocalizedNameOrDescription(
            @Param("searchTerm") String searchTerm,
            @Param("lang") String lang,
            Pageable pageable
    );
}
