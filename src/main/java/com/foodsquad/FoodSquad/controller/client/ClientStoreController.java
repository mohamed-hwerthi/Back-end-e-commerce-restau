package com.foodsquad.FoodSquad.controller.client;


import com.foodsquad.FoodSquad.model.dto.client.ClientStoreDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for client-facing store endpoints (storefront).
 *
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
@RestController
@RequestMapping("/api/client/stores")
@RequiredArgsConstructor
@Slf4j
public class ClientStoreController {

    private final ClientStoreService clientStoreService;

    /**
     * Get a store by ID for the client (storefront).
     *
     * @param storeId the UUID of the store
     * @return ClientStoreDTO with public store information
     */
    @Operation(summary = "Get store by ID for storefront", description = "Returns public store information for clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{storeId}")
    public ResponseEntity<ClientStoreDTO> getStoreById(@PathVariable UUID storeId) {
        log.info("Received request to fetch storeId={}", storeId);
        ClientStoreDTO storeDTO = clientStoreService.getStoreById(storeId);
        log.info("Returning store data for storeId={}", storeId);
        return ResponseEntity.ok(storeDTO);
    }
}
