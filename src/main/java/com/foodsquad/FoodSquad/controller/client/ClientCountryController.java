package com.foodsquad.FoodSquad.controller.client;

import com.foodsquad.FoodSquad.model.dto.client.ClientCountryDTO;
import com.foodsquad.FoodSquad.service.client.ClientCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client/countries")
public class ClientCountryController {

    private final ClientCountryService clientCountryService;

    @Autowired
    public ClientCountryController(ClientCountryService clientCountryService) {
        this.clientCountryService = clientCountryService;
    }

    @GetMapping
    public ResponseEntity<List<ClientCountryDTO>> getAllCountries() {
        List<ClientCountryDTO> countries = clientCountryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }
}
