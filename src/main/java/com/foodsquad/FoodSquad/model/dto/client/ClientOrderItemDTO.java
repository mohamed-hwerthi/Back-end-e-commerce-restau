    package com.foodsquad.FoodSquad.model.dto.client;

    import jakarta.validation.Valid;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import lombok.*;

    import java.math.BigDecimal;
    import java.util.List;
    import java.util.UUID;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class ClientOrderItemDTO {

        @NotNull(message = "Product ID is required")
        private UUID productId;

        @NotBlank(message = "product Name must not be  blank ")
        private String productName;

        @NotNull(message = "Price is required")
        private BigDecimal unitPrice;

        @NotNull(message = "Quantity is required")
        private int quantity;

        @Valid
        private List<String> mediasUrls;

        @Valid
        private List<ClientOrderItemOptionDTO> options;

        @NotNull(message = "Total price is required")
        private BigDecimal totalPrice;

    }
