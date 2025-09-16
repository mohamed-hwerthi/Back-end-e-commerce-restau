package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.LanguageDTO;
import com.foodsquad.FoodSquad.service.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing {@link com.foodsquad.FoodSquad.model.entity.Language}.
 * Provides endpoints to retrieve available languages in the system.
 */
@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Languages", description = "API endpoints for managing languages")
public class LanguageController {

    private final LanguageService languageService;

    /**
     * Retrieve all available languages.
     *
     * @return a list of {@link LanguageDTO}
     */
    @GetMapping
    @Operation(
            summary = "Get all languages",
            description = "Retrieve a list of all available languages in the system."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of languages")
    public ResponseEntity<List<LanguageDTO>> getAllLanguages() {
        log.debug("Request received: Get all languages");

        List<LanguageDTO> languages = languageService.findAll();

        log.info("Returning {} languages", languages.size());
        return ResponseEntity.ok(languages);
    }
}
