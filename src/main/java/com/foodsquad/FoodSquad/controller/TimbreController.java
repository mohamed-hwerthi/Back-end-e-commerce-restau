package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.TimbreDTO;
import com.foodsquad.FoodSquad.service.declaration.TimbreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timbres")
@RequiredArgsConstructor
@Tag(name = "Timbre Management", description = "APIs for managing timbres")
public class TimbreController {

    private static final Logger logger = LoggerFactory.getLogger(TimbreController.class);

    private final TimbreService timbreService;

    @Operation(summary = "Create a new timbre")
    @ApiResponse(responseCode = "200", description = "Timbre created successfully")
    @PostMapping
    public ResponseEntity<TimbreDTO> create(
            @RequestParam String storeId,
            @RequestBody TimbreDTO timbreDTO) {

        logger.info("Received request to create Timbre: {} for storeId: {}", timbreDTO, storeId);

        TimbreDTO saved = timbreService.save(timbreDTO);
        logger.info("Created Timbre with id: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Retrieve all timbres by Store ID")
    @GetMapping
    public ResponseEntity<List<TimbreDTO>> getAll(@RequestParam String storeId) {
        logger.info("Received request to get all Timbres for storeId={}", storeId);
        List<TimbreDTO> timbres = timbreService.findByStoreId(storeId);
        logger.info("Returning {} Timbres for storeId={}", timbres.size(), storeId);
        return ResponseEntity.ok(timbres);
    }

    @Operation(summary = "Retrieve a timbre by ID and Store ID")
    @ApiResponse(responseCode = "200", description = "Timbre found")
    @ApiResponse(responseCode = "404", description = "Timbre not found")
    @GetMapping("/{id}")
    public ResponseEntity<TimbreDTO> getById(
            @PathVariable String id,
            @RequestParam String storeId) {

        logger.info("Received request to get Timbre by id: {} for storeId: {}", id, storeId);
        TimbreDTO timbreDTO = timbreService.findById(id);

        if (timbreDTO == null ) {
            logger.warn("Timbre not found or does not belong to storeId: {}", storeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Found Timbre: {}", timbreDTO);
        return ResponseEntity.ok(timbreDTO);
    }

    @Operation(summary = "Delete a timbre by ID and Store ID")
    @ApiResponse(responseCode = "204", description = "Timbre deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestParam String storeId) {

        logger.info("Received request to delete Timbre id: {} for storeId: {}", id, storeId);
        TimbreDTO timbreDTO = timbreService.findById(id);

        if (timbreDTO == null ) {
            logger.warn("Timbre not found or storeId mismatch for deletion.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        timbreService.delete(id);
        logger.info("Deleted Timbre with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a timbre by ID and Store ID")
    @ApiResponse(responseCode = "200", description = "Timbre updated successfully")
    @ApiResponse(responseCode = "404", description = "Timbre not found")
    @PutMapping("/{id}")
    public ResponseEntity<TimbreDTO> updateTimbre(
            @PathVariable("id") String timberId,
            @RequestParam String storeId,
            @RequestBody TimbreDTO timbreDTO) {

        logger.info("Received request to update Timbre id: {} for storeId: {}", timberId, storeId);


        TimbreDTO updated = timbreService.update(timberId, timbreDTO);

        logger.info("Updated Timbre: {}", updated);
        return ResponseEntity.ok(updated);
    }
}
