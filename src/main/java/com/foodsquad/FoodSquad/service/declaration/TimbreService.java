package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.TimbreDTO;

import java.util.List;

public interface TimbreService {
    TimbreDTO save(TimbreDTO timbreDTO);
    TimbreDTO update(String timberId , TimbreDTO timbreDTO); // ✅ Méthode ajoutée
    List<TimbreDTO> findAll();
    TimbreDTO findById(String id);
    void delete(String id);
}