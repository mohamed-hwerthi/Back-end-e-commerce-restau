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
    public ResponseEntity<TimbreDTO> create(@RequestBody TimbreDTO timbreDTO) {
        logger.info("Received request to create Timbre: {}", timbreDTO);
        TimbreDTO saved = timbreService.save(timbreDTO);
        logger.info("Created Timbre with id: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Retrieve all timbres")
    @GetMapping
    public ResponseEntity<List<TimbreDTO>> getAll() {
        logger.info("Received request to get all Timbres");
        List<TimbreDTO> timbres = timbreService.findAll();
        logger.info("Returning {} Timbres", timbres.size());
        return ResponseEntity.ok(timbres);
    }

    @Operation(summary = "Retrieve a timbre by ID")
    @ApiResponse(responseCode = "200", description = "Timbre found")
    @ApiResponse(responseCode = "404", description = "Timbre not found")
    @GetMapping("/{id}")
    public ResponseEntity<TimbreDTO> getById(@PathVariable String id) {
        logger.info("Received request to get Timbre by id: {}", id);
        TimbreDTO timbreDTO = timbreService.findById(id);
        if (timbreDTO == null) {
            logger.warn("Timbre not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Found Timbre: {}", timbreDTO);
        return ResponseEntity.ok(timbreDTO);
    }

    @Operation(summary = "Delete a timbre by ID")
    @ApiResponse(responseCode = "204", description = "Timbre deleted successfully")
    @ApiResponse(responseCode = "404", description = "Timbre not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("Received request to delete Timbre id: {}", id);
        TimbreDTO timbreDTO = timbreService.findById(id);
        if (timbreDTO == null) {
            logger.warn("Timbre not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        timbreService.delete(id);
        logger.info("Deleted Timbre with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a timbre by ID")
    @ApiResponse(responseCode = "200", description = "Timbre updated successfully")
    @ApiResponse(responseCode = "404", description = "Timbre not found")
    @PutMapping("/{id}")
    public ResponseEntity<TimbreDTO> updateTimbre(@PathVariable("id") String id, @RequestBody TimbreDTO timbreDTO) {
        logger.info("Received request to update Timbre id: {}", id);
        TimbreDTO updated = timbreService.update(id, timbreDTO);
        logger.info("Updated Timbre: {}", updated);
        return ResponseEntity.ok(updated);
    }
}