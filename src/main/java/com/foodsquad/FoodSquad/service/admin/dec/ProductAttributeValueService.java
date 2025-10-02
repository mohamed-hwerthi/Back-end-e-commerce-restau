package com.foodsquad.FoodSquad.service.admin.dec;


import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;

public interface ProductAttributeValueService {

    ProductAttributeValue findOrCreateValue(ProductAttribute attribute, String valueStr);

}