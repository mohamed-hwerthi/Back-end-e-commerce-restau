package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.CustomAttributeDTO;

import java.util.List;
import java.util.UUID;

public interface CustomAttributeService {

    List<CustomAttributeDTO> findAll();

    CustomAttributeDTO findById(UUID id);

    void delete(UUID id);
}
