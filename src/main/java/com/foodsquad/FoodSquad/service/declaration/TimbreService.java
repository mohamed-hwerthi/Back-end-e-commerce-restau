package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.TimbreDTO;

import java.util.List;
import java.util.UUID;

public interface TimbreService {

    TimbreDTO save(TimbreDTO timbreDTO);

    TimbreDTO update(UUID timberId, TimbreDTO timbreDTO);

    List<TimbreDTO> findAll();

    TimbreDTO findById(UUID id);

    void delete(UUID id);



}