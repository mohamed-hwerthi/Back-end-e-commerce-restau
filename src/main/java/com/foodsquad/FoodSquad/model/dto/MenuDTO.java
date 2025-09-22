package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class MenuDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @NotBlank(message = "  menu  name  cannot be blank")
    private String name;
    private String description;
    private String qrCode;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<UUID> menuItemsIds = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProductDTO> products = new ArrayList<>();


}
