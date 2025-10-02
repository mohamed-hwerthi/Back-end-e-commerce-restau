package com.foodsquad.FoodSquad.model.dto;


import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.validation.annotations.NotEmptyLocalizedString;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
public class CategoryDTO {
    private UUID id;
    @NotEmptyLocalizedString(message = "Category title must have at leat  one translated field")
    private LocalizedString name;
    private LocalizedString description;
    private List<MediaDTO> medias = new ArrayList<>();
}
