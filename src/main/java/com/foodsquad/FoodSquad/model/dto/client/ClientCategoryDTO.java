package com.foodsquad.FoodSquad.model.dto.client;

import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * DTO representing category data exposed to clients (storefront).
 * Contains only public information.
 *
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
@Getter
@Setter
@Builder
public class ClientCategoryDTO {

    private UUID id;

    private String name;

    private String description;

    private List<MediaDTO> medias;
}
