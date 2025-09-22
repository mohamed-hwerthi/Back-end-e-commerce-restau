package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.ProductVariantDTO;

import java.util.List;
import java.util.UUID;

public interface ProductVariantService {

    ProductVariantDTO createVariant(ProductVariantDTO variantDTO);

    ProductVariantDTO getVariantById(UUID id);

    List<ProductVariantDTO> getVariantsByProductId(UUID productId);

    List<ProductVariantDTO> getAllVariants();

    ProductVariantDTO updateVariant(UUID id, ProductVariantDTO variantDTO);

    void deleteVariant(UUID id);

    ProductVariantDTO updateStock(UUID id, Integer quantity);

    ProductVariantDTO toggleVariantStatus(UUID id, boolean isActive);
}
