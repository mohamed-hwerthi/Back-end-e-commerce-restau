package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import com.foodsquad.FoodSquad.repository.ProductAttributeValueRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductAttributeValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAttributeValueServiceImpl implements ProductAttributeValueService {

    private final ProductAttributeValueRepository productAttributeValueRepository;

    /**
     * Finds an attribute value for a given attribute, or creates a new one if it doesn't exist.
     */
    public ProductAttributeValue findOrCreateValue(ProductAttribute attribute, String valueStr) {
        return attribute.getValues().stream()
                .filter(v -> v.getValue().equals(valueStr))
                .findFirst()
                .orElseGet(() -> createValue(attribute, valueStr));
    }

    /**
     * Creates and saves a new ProductAttributeValue for the given attribute.
     */
    private ProductAttributeValue createValue(ProductAttribute attribute, String valueStr) {
        ProductAttributeValue newValue = new ProductAttributeValue();
        newValue.setValue(valueStr);
        newValue.setProductAttribute(attribute);

        ProductAttributeValue savedValue = productAttributeValueRepository.save(newValue);
        attribute.getValues().add(savedValue);
        return savedValue;
    }

}
