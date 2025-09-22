package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttributeValueRepository extends JpaRepository<ProductAttributeValue, UUID> {

    List<ProductAttributeValue> findByAttributeTypeId(UUID attributeTypeId);

    Optional<ProductAttributeValue> findByValueAndAttributeTypeId(String value, UUID attributeTypeId);

    boolean existsByValueAndAttributeTypeId(String value, UUID attributeTypeId);

    List<ProductAttributeValue> findByAttributeTypeIdIn(List<UUID> attributeTypeIds);
}
