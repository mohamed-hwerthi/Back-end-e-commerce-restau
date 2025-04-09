package com.foodsquad.FoodSquad.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CustomResponseDTO {
    @NotBlank(message = "Message cannot be blank")
    private String    message   ;
    @NotBlank(message = "Status cannot be blank")
    private  int     statusCode;






}
