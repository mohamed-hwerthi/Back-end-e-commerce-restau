package com.foodsquad.FoodSquad.repository;
import com.foodsquad.FoodSquad.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository  extends JpaRepository<Currency, Long> {


    Optional<Currency> findBySymbol(String s);
}
