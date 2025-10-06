package com.foodsquad.FoodSquad.model.dto.client;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientProductOptionGroup {

    private LocalizedString name;
    private List<ClientProductOption> options = new ArrayList<>();


}
