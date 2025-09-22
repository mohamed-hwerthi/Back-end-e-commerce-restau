package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.VariantAttributeDTO;

import java.util.List;
import java.util.UUID;

public interface VariantAttributeService {


    VariantAttributeDTO getVariantAttributeById(UUID id);

    List<VariantAttributeDTO> getVariantAttributesByVariantId(UUID variantId);

    void deleteVariantAttribute(UUID id);

    void deleteAllByVariantId(UUID variantId);
}
