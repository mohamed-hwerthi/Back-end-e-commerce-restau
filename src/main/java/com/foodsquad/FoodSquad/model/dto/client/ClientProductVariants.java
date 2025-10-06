package com.foodsquad.FoodSquad.model.dto.client;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientProductVariants {

    private LocalizedString variantName;

    private List<ClientProductVariantOption> options = new ArrayList<>();


}
