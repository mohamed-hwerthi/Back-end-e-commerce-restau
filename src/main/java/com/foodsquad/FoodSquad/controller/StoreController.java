package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.dto.StoreBasicDataDTO;
import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.service.declaration.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "Store Management", description = "APIs for managing stores")
@Slf4j
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final StoreService storeService;

    @Operation(summary = "Create a new store")
    @ApiResponse(responseCode = "200", description = "Store created successfully")
    @PostMapping
    public ResponseEntity<StoreDTO> create(@RequestBody @Valid StoreDTO storeDTO) {
        logger.info("Received request to create Store: {}", storeDTO);
        if (storeDTO.getId() != null && StringUtils.hasText(storeDTO.getId().toString())) {
            throw new IllegalArgumentException("ID must not be sent when creating a new store.");
        }
        StoreDTO saved = storeService.save(storeDTO);
        logger.info("Created Store with id: {}", saved.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);


    }

    @Operation(summary = "Update a store by ID")
    @ApiResponse(responseCode = "200", description = "Store updated successfully")
    @ApiResponse(responseCode = "404", description = "Store not found")
    @PutMapping("/{id}")
    public ResponseEntity<StoreDTO> updateStore(
            @PathVariable("id") UUID id,
            @RequestBody StoreDTO storeDTO) {
        logger.info("Received request to update Store id: {}", id);
        StoreDTO updated = storeService.update(id, storeDTO);
        if (ObjectUtils.isEmpty(updated)) {
            logger.warn("Store not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Updated Store: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Retrieve all stores")
    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAll() {
        logger.info("Received request to get all Stores");
        List<StoreDTO> stores = storeService.findAll();
        logger.info("Returning {} Stores", stores.size());
        return ResponseEntity.ok(stores);
    }

    @Operation(summary = "Retrieve a store by ID")
    @ApiResponse(responseCode = "200", description = "Store found")
    @ApiResponse(responseCode = "404", description = "Store not found")
    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getById(@PathVariable UUID id) {
        logger.info("Received request to get Store by id: {}", id);
        StoreDTO storeDTO = storeService.findById(id);
        if (storeDTO == null) {
            logger.warn("Store not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Found Store: {}", storeDTO);
        return ResponseEntity.ok(storeDTO);
    }

    @Operation(summary = "Delete a store by ID")
    @ApiResponse(responseCode = "204", description = "Store deleted successfully")
    @ApiResponse(responseCode = "404", description = "Store not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        logger.info("Received request to delete Store id: {}", id);
        StoreDTO storeDTO = storeService.findById(id);
        if (storeDTO == null) {
            logger.warn("Store not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        storeService.delete(id);
        logger.info("Deleted Store with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-owner-email")
    @Operation(
            summary = "Find store by owner's email",
            description = "Retrieves the store information for a store owner by their email"
    )
    public ResponseEntity<StoreBasicDataDTO> getStoreByOwnerEmail() {

        log.info("Received request to get store by owner's email");
        StoreBasicDataDTO storeDTO = storeService.findByEmail();
        log.info("Found store with ID: {}", storeDTO.getStoreId());
        return ResponseEntity.ok(storeDTO);
    }


    /**
     * Get the currency of a specific store.
     *
     * @param storeId UUID of the store
     * @return ResponseEntity containing the CurrencyDTO
     */
    @GetMapping("/{storeId}/currency")
    @Operation(summary = "Get store currency", description = "Retrieve the currency associated with a store")
    public ResponseEntity<CurrencyDTO> getCurrencyOfStore(@PathVariable UUID storeId) {
        CurrencyDTO currency = storeService.findCurrencyOfStore(storeId);
        if (currency == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(currency);
    }

    @GetMapping("/by-slug/{slug}")
    @Operation(
            summary = "Find store by slug",
            description = "Retrieves the store information by store slug"
    )
    public ResponseEntity<StoreBasicDataDTO> getStoreBySlug(@PathVariable String slug) {
        log.info("Received request to get store by slug: {}", slug);
        StoreBasicDataDTO storeDTO = storeService.findByStoreSlug(slug);
        log.info("Found store with ID: {}", storeDTO.getStoreId());
        return ResponseEntity.ok(storeDTO);
    }


}
