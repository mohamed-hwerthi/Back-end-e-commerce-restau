package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, UUID> {

    List<VariantAttribute> findByVariantId(UUID variantId);

    Optional<VariantAttribute> findByVariantIdAndAttributeValueId(UUID variantId, UUID attributeValueId);

    boolean existsByVariantIdAndAttributeValueId(UUID variantId, UUID attributeValueId);

    void deleteByVariantId(UUID variantId);
}
