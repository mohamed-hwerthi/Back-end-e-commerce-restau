package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class StoreOwnerAuthResponse {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Role is required")
    @Schema(description = "Role of the user", example = "ADMIN", required = true)
    private String role;

    @Schema(description = "URL of the user's profile image", example = "http://example.com/image.jpg")
    private String imageUrl;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    @Schema(description = "Phone number of the user", example = "+359 899 78 7878")
    private String phoneNumber;

    private String storeId;
}
