package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor


public class MediaDTO {
    private Long id;
    @NotBlank(message = "name  cannot be blank")
    private String name  ;
    private String url  ;
    private String path  ;
    private String type  ;


}
