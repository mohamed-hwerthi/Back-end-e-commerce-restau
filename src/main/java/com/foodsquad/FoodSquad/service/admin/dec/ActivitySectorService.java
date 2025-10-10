package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.ActivitySectorDTO;

import java.util.List;
import java.util.UUID;

public interface ActivitySectorService {

    ActivitySectorDTO save(ActivitySectorDTO activitySectorDTO);

    ActivitySectorDTO update(String id, ActivitySectorDTO activitySectorDTO);

    List<ActivitySectorDTO> findAll();

    ActivitySectorDTO findById(String id);

    void delete(String id);
}