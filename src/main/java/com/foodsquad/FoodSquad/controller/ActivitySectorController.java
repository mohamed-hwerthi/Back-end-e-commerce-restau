package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.ActivitySectorDTO;
import com.foodsquad.FoodSquad.service.declaration.ActivitySectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/activity-sectors")
@RequiredArgsConstructor
@Tag(
        name = "Activity Sector Management",
        description = "APIs for managing activity sectors"
)
public class ActivitySectorController {

    private final ActivitySectorService activitySectorService;

    @Operation(summary = "Create a new activity sector")
    @ApiResponse(responseCode = "201", description = "Activity sector created successfully")
    @PostMapping
    public ResponseEntity<ActivitySectorDTO> create(
            @Valid @RequestBody ActivitySectorDTO activitySectorDTO) {
        log.info("Creating ActivitySector: {}", activitySectorDTO);
        ActivitySectorDTO created = activitySectorService.save(activitySectorDTO);
        log.info("Created ActivitySector: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }



    @Operation(summary = "Get an activity sector by ID")
    @ApiResponse(responseCode = "200", description = "Activity sector found")
    @ApiResponse(responseCode = "404", description = "Activity sector not found")
    @GetMapping("/{id}")
    public ResponseEntity<ActivitySectorDTO> getById(
            @Parameter(description = "UUID of the ActivitySector")
            @PathVariable UUID id) {
        log.info("Fetching ActivitySector with id: {}", id);
        ActivitySectorDTO activitySectorDTO = activitySectorService.findById(id);
        log.info("Found ActivitySector: {}", activitySectorDTO);
        return ResponseEntity.ok(activitySectorDTO);
    }

    @Operation(summary = "Update an activity sector by ID")
    @ApiResponse(responseCode = "200", description = "Activity sector updated successfully")
    @ApiResponse(responseCode = "404", description = "Activity sector not found")
    @PutMapping("/{id}")
    public ResponseEntity<ActivitySectorDTO> update(
            @Parameter(description = "UUID of the ActivitySector")
            @PathVariable UUID id,
            @Valid @RequestBody ActivitySectorDTO activitySectorDTO) {
        log.info("Updating ActivitySector with id: {}", id);
        ActivitySectorDTO updated = activitySectorService.update(id, activitySectorDTO);
        log.info("Updated ActivitySector: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete an activity sector by ID")
    @ApiResponse(responseCode = "204", description = "Activity sector deleted successfully")
    @ApiResponse(responseCode = "404", description = "Activity sector not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID of the ActivitySector")
            @PathVariable UUID id) {
        log.info("Deleting ActivitySector with id: {}", id);
        activitySectorService.delete(id);
        log.info("Deleted ActivitySector with id: {}", id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get all activity sectors", description = "Retrieve a list of all available activity sectors")
    @ApiResponse(responseCode = "200", description = "List of activity sectors retrieved successfully")
    @GetMapping
    public ResponseEntity<List<ActivitySectorDTO>> findAll() {
        log.info("Fetching all ActivitySectors");
        List<ActivitySectorDTO> sectors = activitySectorService.findAll();
        log.info("Fetched {} ActivitySectors", sectors.size());
        return ResponseEntity.ok(sectors);
    }

}
