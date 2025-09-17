package com.foodsquad.FoodSquad.model.dto;


import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
public class CategoryDTO {
    private UUID id;
    @NotBlank(message = "name  cannot be blank")
    private LocalizedString name;
    private LocalizedString description;
    private List<MediaDTO> medias = new ArrayList<>();
}
