package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductAttribute;

public interface ProductAttributeService
{
     ProductAttribute findOrCreateAttribute(Product product, LocalizedString attributeName) ;




}
