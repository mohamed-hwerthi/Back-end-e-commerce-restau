package com.foodsquad.FoodSquad.model.dto.client;

import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO representing product data exposed to clients (storefront).
 * Contains only public information.
 *
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
@Getter
@Setter
@Builder
public class ClientProductDTO {

    private UUID id;

    private LocalizedString title;

    private LocalizedString description;

    private BigDecimal price;

    private boolean inStock;

    private List<MediaDTO> medias;

    private List<ClientCategoryDTO> categories;
}
