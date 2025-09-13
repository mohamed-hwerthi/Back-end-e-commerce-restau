package com.foodsquad.FoodSquad.repository;


import com.foodsquad.FoodSquad.model.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
    public interface MediaRepository extends JpaRepository<Media, UUID> {

    }
