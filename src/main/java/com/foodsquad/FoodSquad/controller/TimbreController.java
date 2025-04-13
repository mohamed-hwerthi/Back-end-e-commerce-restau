package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.TimbreDTO;
import com.foodsquad.FoodSquad.service.declaration.TimbreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timbres")
@RequiredArgsConstructor
@Tag(name = "Timbre Management", description = "APIs for managing timbres")
public class TimbreController {

    private final TimbreService timbreService;

    @Operation(summary = "Create a new timbre")
    @PostMapping
    public ResponseEntity<TimbreDTO> create(@RequestBody TimbreDTO timbreDTO) {
        return ResponseEntity.ok(timbreService.save(timbreDTO));
    }

    @Operation(summary = "Retrieve all timbres")
    @GetMapping
    public ResponseEntity<List<TimbreDTO>> getAll() {
        return ResponseEntity.ok(timbreService.findAll());
    }

    @Operation(summary = "Retrieve a timbre by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TimbreDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(timbreService.findById(id));
    }

    @Operation(summary = "Delete a timbre by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        timbreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
