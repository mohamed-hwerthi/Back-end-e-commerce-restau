package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySectorDTO {

    @NotBlank(message = "Activity sector ID must not be blank")
    @Pattern(regexp = "^[A-Z]{2,10}$", message = "Activity sector ID must be an uppercase code (e.g., TN, EN, IND)")
    private String id;

    @NotBlank(message = "Activity sector code must not be blank")
    @Pattern(regexp = "^[A-Z0-9_-]{2,20}$", message = "Code must contain only uppercase letters, numbers, underscores, or hyphens")
    private String code;

    @NotNull(message = "Activity sector name must not be null")
    @Valid
    private LocalizedString name;
}
