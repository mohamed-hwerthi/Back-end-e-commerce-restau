package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.service.declaration.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/currencies")

@Tag(name = "11. Currency Management", description = "Currency Management API")
public class CurrencyController {


    private final CurrencyService currencyService;

    @Operation(summary = "Get all Currency", description = "Retrieve all Currency available in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all Currency",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class))))
    })
    @GetMapping()
    public ResponseEntity<List<CurrencyDTO>> findAllCategories() {
        return ResponseEntity.ok().body(currencyService.findAllCurrency());
    }
}
