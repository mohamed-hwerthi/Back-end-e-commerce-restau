package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    Optional<Country> findByCode(String code);

    Optional<Country> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);

}
