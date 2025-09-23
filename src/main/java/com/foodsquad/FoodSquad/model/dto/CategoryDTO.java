package com.foodsquad.FoodSquad.model.dto;


import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
public class CategoryDTO {
    private UUID id;
    @NotEmpty(message = "name  cannot empty ")
    private LocalizedString name;
    private LocalizedString description;
    private List<MediaDTO> medias = new ArrayList<>();
}
