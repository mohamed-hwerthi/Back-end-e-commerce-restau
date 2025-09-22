package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.AttributeValueDTO;

import java.util.List;
import java.util.UUID;

public interface ProductAttributeValueService {

    AttributeValueDTO createAttributeValue(AttributeValueDTO attributeValueDTO);

    AttributeValueDTO getAttributeValueById(UUID id);

    List<AttributeValueDTO> getAttributeValuesByType(UUID attributeTypeId);

    List<AttributeValueDTO> getAllAttributeValues();

    AttributeValueDTO updateAttributeValue(UUID id, AttributeValueDTO attributeValueDTO);

    void deleteAttributeValue(UUID id);

    List<AttributeValueDTO> getAttributeValuesByTypeIn(List<UUID> attributeTypeIds);
}