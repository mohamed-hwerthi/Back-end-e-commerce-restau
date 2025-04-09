package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class MediaDTO {
    private Long id;
    @NotBlank(message = "name  cannot be blank")
    private String name  ;
    private String url  ;
    private String path  ;
    private String type  ;


}
