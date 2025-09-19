package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {

    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String slug;

    @NotBlank(message = "PhonePhone number  is required")
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String address;

    private String facebookUrl;
    private String instagramUrl;
    private String linkedInUrl;
    private String websiteUrl;

    private String about;
    private String backgroundColor;
    private String templateName;
    private String accentColor;

    private boolean active;
    private LocalDateTime createdAt;

    @NotNull(message = "Activity sector is required")
    private ActivitySectorDTO activitySector;
    private MediaDTO logo;

    @NotNull(message = "Currency is required")
    private CurrencyDTO currency;

    private String encryptedStoreId;

    private LanguageDTO defaultLanguage;


}
