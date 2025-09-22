package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.ProductVariantMapper;
import com.foodsquad.FoodSquad.model.dto.ProductVariantDTO;
import com.foodsquad.FoodSquad.model.entity.ProductVariant;
import com.foodsquad.FoodSquad.repository.ProductVariantRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductVariantService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductVariantMapper variantMapper;

    @Override
    @Transactional
    public ProductVariantDTO createVariant(ProductVariantDTO variantDTO) {
        log.info("Creating new product variant with SKU: {}", variantDTO.getSku());
        checkSkuExists(variantDTO.getSku());

        ProductVariant variant = variantMapper.toEntity(variantDTO);
        saveAttributesIfPresent(variant, variantDTO);

        ProductVariant savedVariant = variantRepository.save(variant);
        log.info("Product variant created with ID: {}", savedVariant.getId());

        return variantMapper.toDto(savedVariant);
    }

    @Override
    public ProductVariantDTO getVariantById(UUID id) {
        log.info("Fetching product variant with ID: {}", id);
        ProductVariant variant = findVariantById(id);
        log.info("Fetched product variant: {}", variant.getSku());
        return variantMapper.toDto(variant);
    }




    @Override
    @Transactional
    public ProductVariantDTO updateVariant(UUID id, ProductVariantDTO variantDTO) {
        log.info("Updating product variant with ID: {}", id);
        ProductVariant variant = findVariantById(id);

        variant.setSku(variantDTO.getSku());
        variant.setPrice(variantDTO.getPrice());
        variant.setQuantity(variantDTO.getQuantity());


        ProductVariant updatedVariant = variantRepository.save(variant);
        log.info("Updated product variant with ID: {}", id);
        return variantMapper.toDto(updatedVariant);
    }

    @Override
    @Transactional
    public void deleteVariant(UUID id) {
        log.info("Deleting product variant with ID: {}", id);
        if (!variantRepository.existsById(id)) {
            log.warn("Product variant with ID {} not found", id);
            throw new EntityNotFoundException("Product variant not found with ID: " + id);
        }
        variantRepository.deleteById(id);
        log.info("Deleted product variant with ID: {}", id);
    }

    @Override
    @Transactional
    public ProductVariantDTO updateStock(UUID id, Integer quantity) {
        log.info("Updating stock for product variant with ID: {} by quantity: {}", id, quantity);
        ProductVariant variant = findVariantById(id);

        int newQuantity = variant.getQuantity() + quantity;
        if (newQuantity < 0) {
            log.error("Insufficient stock for variant ID: {}. Current: {}, Requested change: {}", id, variant.getQuantity(), quantity);
            throw new IllegalArgumentException("Insufficient stock");
        }

        variant.setQuantity(newQuantity);
        ProductVariant updatedVariant = variantRepository.save(variant);
        log.info("Updated stock for variant ID: {}. New quantity: {}", id, newQuantity);
        return variantMapper.toDto(updatedVariant);
    }

    @Override
    @Transactional
    public ProductVariantDTO toggleVariantStatus(UUID id, boolean isActive) {
        log.info("{} product variant with ID: {}", isActive ? "Activating" : "Deactivating", id);
        ProductVariant variant = findVariantById(id);

        ProductVariant updatedVariant = variantRepository.save(variant);

        log.info("{} product variant with ID: {}", isActive ? "Activated" : "Deactivated", id);
        return variantMapper.toDto(updatedVariant);
    }

    // =======================
    // Private Helper Methods
    // =======================

    private void checkSkuExists(String sku) {
        if (variantRepository.existsBySku(sku)) {
            log.warn("SKU {} already exists", sku);
            throw new IllegalArgumentException("A variant with SKU " + sku + " already exists");
        }
    }

    private void saveAttributesIfPresent(ProductVariant variant, ProductVariantDTO dto) {

    }

    private ProductVariant findVariantById(UUID id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ProductVariant not found with ID: {}", id);
                    return new EntityNotFoundException("ProductVariant not found with ID: " + id);
                });
    }
}
