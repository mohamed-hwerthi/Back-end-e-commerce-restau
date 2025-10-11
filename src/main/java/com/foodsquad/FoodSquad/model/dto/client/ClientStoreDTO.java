package com.foodsquad.FoodSquad.model.dto.client;

import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing store data exposed to clients (storefront).
 * Contains only public information, without sensitive fields.
 *
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
@Getter
@Setter
@Builder
public class ClientStoreDTO {


    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String postalCode;

    private String facebookUrl;

    private String instagramUrl;

    private String linkedInUrl;

    private String websiteUrl;

    private String whatsapp;

    private String emailContact;

    private String about;

    private  String logo;

    private String currencySymbol;

    private String countryName;

    private String city;
}
