package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CountryDTO;
import com.foodsquad.FoodSquad.model.entity.Country;
import com.foodsquad.FoodSquad.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
@Tag(name = "Country Management", description = "APIs for managing countries")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
    private final CountryService countryService;

    @Operation(summary = "Create a new country")
    @ApiResponse(responseCode = "200", description = "Country created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<Country> createCountry(@RequestBody CountryDTO countryDTO) {
        logger.info("Received request to create country: {}", countryDTO.getName());
        Country created = countryService.save(countryDTO);
        logger.info("Created country with code: {}", created.getCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get all countries")
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        logger.info("Received request to get all countries");
        List<Country> countries = countryService.findAll();
        logger.info("Returning {} countries", countries.size());
        return ResponseEntity.ok(countries);
    }

    @Operation(summary = "Get a country by code")
    @ApiResponse(responseCode = "200", description = "Country found")
    @ApiResponse(responseCode = "404", description = "Country not found")
    @GetMapping("/{code}")
    public ResponseEntity<Country> getCountryByCode(@PathVariable String code) {
        logger.info("Received request to get country with code: {}", code);
        Country country = countryService.findByCode(code);
        return ResponseEntity.ok(country);
    }

    @Operation(summary = "Update a country")
    @ApiResponse(responseCode = "200", description = "Country updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Country not found")
    @PutMapping("/{code}")
    public ResponseEntity<Country> updateCountry(
            @PathVariable String code,
            @RequestBody CountryDTO countryDTO) {
        logger.info("Received request to update country with code: {}", code);
        Country updated = countryService.update(code, countryDTO);
        logger.info("Updated country with code: {}", code);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a country")
    @ApiResponse(responseCode = "204", description = "Country deleted successfully")
    @ApiResponse(responseCode = "404", description = "Country not found")
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCountry(@PathVariable String id) {
        logger.info("Received request to delete country with id: {}", id);
        countryService.delete(id);
        logger.info("Deleted country with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
