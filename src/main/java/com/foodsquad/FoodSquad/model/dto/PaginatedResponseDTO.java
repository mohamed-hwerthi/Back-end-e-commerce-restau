package com.foodsquad.FoodSquad.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class PaginatedResponseDTO<T> {
    private List<T> items;
    private long totalCount;


}
