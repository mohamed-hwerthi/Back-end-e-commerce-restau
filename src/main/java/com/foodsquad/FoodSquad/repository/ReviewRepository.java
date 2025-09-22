package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(UUID productId);

    List<Review> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    long countByProductId(UUID productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(UUID productId);

}
