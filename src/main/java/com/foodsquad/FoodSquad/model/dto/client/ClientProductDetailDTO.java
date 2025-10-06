package com.foodsquad.FoodSquad.model.dto.client;

import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Detailed DTO for client-facing product view.
 * Includes variants and option groups suitable for product detail page.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientProductDetailDTO {

    private UUID id;

    private LocalizedString title;

    private LocalizedString description;

    private BigDecimal basePrice;

    private BigDecimal discountedPrice;

    private String isPromoted;

    private boolean inStock;

    private List<String> mediasUrls;

    private LocalizedString categoryName;

    private List<ClientProductVariants> variants;

    private List<ClientProductOptionGroup> optionGroups;
}
