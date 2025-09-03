package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.ActivitySectorDTO;

import java.util.List;
import java.util.UUID;

public interface ActivitySectorService {

    ActivitySectorDTO save(ActivitySectorDTO activitySectorDTO);

    ActivitySectorDTO update(UUID id, ActivitySectorDTO activitySectorDTO);

    List<ActivitySectorDTO> findAll();

    ActivitySectorDTO findById(UUID id);

    void delete(UUID id);
}