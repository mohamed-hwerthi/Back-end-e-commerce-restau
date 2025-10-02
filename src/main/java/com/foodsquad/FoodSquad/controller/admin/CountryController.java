package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
@Tag(name = "Country Management", description = "APIs for managing countries")
@Slf4j
public class CountryController {

    private final CountryService countryService;


    @Operation(summary = "Get all countries")
    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        log.info("Received request to get all countries");
        List<CountryDTO> countries = countryService.findAll();
        log.info("Returning {} countries", countries.size());
        return ResponseEntity.ok(countries);
    }

}
