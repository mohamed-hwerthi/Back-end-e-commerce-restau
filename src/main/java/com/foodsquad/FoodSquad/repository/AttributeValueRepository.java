package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, UUID> {
    
    List<AttributeValue> findByAttributeTypeId(UUID attributeTypeId);
    
    Optional<AttributeValue> findByValueAndAttributeTypeId(String value, UUID attributeTypeId);
    
    boolean existsByValueAndAttributeTypeId(String value, UUID attributeTypeId);
    
    List<AttributeValue> findByAttributeTypeIdIn(List<UUID> attributeTypeIds);
}
