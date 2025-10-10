package com.foodsquad.FoodSquad.model.dto;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {

    @NotBlank(message = "Currency ID must not be blank")
    @Pattern(regexp = "^[A-Z]{2,5}$", message = "Currency ID must be an uppercase code (e.g., USD, EUR, TND)")
    private String id;

    @NotNull(message = "Currency name must not be null")
    @Valid
    private LocalizedString name;

    @NotBlank(message = "Currency symbol must not be blank")
    @Size(max = 5, message = "Currency symbol must be at most 5 characters long")
    private String symbol;

    @Min(value = 0, message = "Scale must be zero or greater")
    @Max(value = 4, message = "Scale must not exceed 4")
    private int scale;
}
