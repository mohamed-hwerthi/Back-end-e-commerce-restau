package com.foodsquad.FoodSquad.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodsquad.FoodSquad.model.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
public class OrderDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotNull(message = "Menu item quantities are required")
    @Schema(example = "{\"1\": 1, \"2\": 2}")
    private Map<UUID, Integer> menuItemQuantities;

    @NotNull(message = "Status is required")
    @Schema(example = "PENDING")
    private OrderStatus status;

    @Positive(message = "Total cost must be positive")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalCost;

    @NotNull(message = "Creation date is required")
    private LocalDateTime createdAt;

    @Schema(defaultValue = "false")
    private Boolean paid = false;

    @Schema(example = "admin@example.com")
    private String userEmail;

    public OrderDTO() {
    }

    public OrderDTO(String id, String userEmail, Map<UUID, Integer> menuItemQuantities, OrderStatus status, BigDecimal totalCost, LocalDateTime createdOn, Boolean paid) {
        this.id = id;
        this.userEmail = userEmail;
        this.menuItemQuantities = menuItemQuantities;
        this.status = status;
        this.totalCost = totalCost;
        this.createdAt = createdOn;
        this.paid = paid;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status.name();
    }

    public void setStatus(@NotNull(message = "Status is required") OrderStatus status) {
        this.status = status;
    }


    public @NotNull(message = "Creation date is required") LocalDateTime getCreatedOn() {
        return createdAt;
    }

    public void setCreatedOn(@NotNull(message = "Creation date is required") LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
