package com.foodsquad.FoodSquad.controller.client;


import com.foodsquad.FoodSquad.model.dto.client.ClientStoreDTO;
import com.foodsquad.FoodSquad.service.client.dec.ClientStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param slug the UUID of the store
     * @return ClientStoreDTO with public store information
     */
    @Operation(
            summary = "Find store by slug",
            description = "Retrieves the store information by store slug"
    )
    @GetMapping("/by-slug/{slug}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store found"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    public ResponseEntity<ClientStoreDTO> getStoreBySlug(@PathVariable String slug) {
        log.info("Received request to get store by slug: {}", slug);
        ClientStoreDTO clientStoreDTO = clientStoreService.getStoreBySlug(slug);
        log.info("Found store with ID: {}", clientStoreDTO.toString());
        return ResponseEntity.ok(clientStoreDTO);
    }
}
