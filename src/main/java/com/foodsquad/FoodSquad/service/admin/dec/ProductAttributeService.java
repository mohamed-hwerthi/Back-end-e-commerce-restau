package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.ProductAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductAttribute;

import java.util.UUID;

public interface ProductAttributeService {
    ProductAttribute findOrCreateAttribute(Product product, LocalizedString attributeName);

    ProductAttribute createAttribute(ProductAttribute productAttribute);

    ProductAttributeDTO updateProductAttributeName(UUID productAttributeId, LocalizedString name);


}
