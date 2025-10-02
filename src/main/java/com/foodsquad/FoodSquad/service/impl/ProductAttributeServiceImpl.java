package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.ProductAttributeMapper;
import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import com.foodsquad.FoodSquad.repository.ProductAttributeRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeMapper productAttributeMapper;

    /**
     * Finds an attribute by name for a product, or creates a new one if it doesn't exist.
     */
    public ProductAttribute findOrCreateAttribute(Product product, LocalizedString attributeName) {
        return product.getAttributes().stream()
                .filter(a -> a.getName().equals(attributeName))
                .findFirst()
                .orElseGet(() -> createAttribute(product, attributeName));
    }

    @Override
    public ProductAttribute createAttribute(ProductAttribute productAttribute) {
        log.info("Creating attribute: {}", productAttribute);
        return productAttributeRepository.save(productAttribute);

    }

    @Override
    public ProductAttributeDTO updateProductAttributeName(UUID productAttributeId, LocalizedString name) {
        log.debug("Updating ProductAttribute with id: {} to new name: {}", productAttributeId, name);

        ProductAttribute attribute = productAttributeRepository.findById(productAttributeId)
                .orElseThrow(() -> {
                    log.error("ProductAttribute not found for id: {}", productAttributeId);
                    return new IllegalArgumentException("Product attribute not found with id: " + productAttributeId);
                });

        attribute.setName(name);

        ProductAttribute updatedAttribute = productAttributeRepository.save(attribute);

        ProductAttributeDTO dto = productAttributeMapper.toDto(updatedAttribute);

        log.info("Successfully updated ProductAttribute id: {} with new name: {}", updatedAttribute.getId(), updatedAttribute.getName());

        return dto;
    }


    /**
     * Creates and saves a new ProductAttribute for the given product.
     */
    private ProductAttribute createAttribute(Product product, LocalizedString attributeName) {
        ProductAttribute newAttr = new ProductAttribute();
        newAttr.setName(attributeName);
        newAttr.setProduct(product);

        ProductAttribute savedAttr = productAttributeRepository.save(newAttr);
        product.getAttributes().add(savedAttr);
        return savedAttr;
    }


}
