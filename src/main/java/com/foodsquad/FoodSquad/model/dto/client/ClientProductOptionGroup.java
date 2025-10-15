package com.foodsquad.FoodSquad.model.dto.client;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientProductOptionGroup {

    private String name;
    private List<ClientProductOptionDTO> options = new ArrayList<>();


}
