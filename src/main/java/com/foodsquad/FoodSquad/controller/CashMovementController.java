package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementResponseDTO;
import com.foodsquad.FoodSquad.service.dec.CashMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cash-movements")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cash Movement Management", description = "APIs for managing cash movements")
public class CashMovementController {

    private final CashMovementService cashMovementService;

    @PostMapping
    @Operation(summary = "Create a new cash movement")
    public ResponseEntity<CashMovementResponseDTO> createCashMovement(
            @Valid @RequestBody CashMovementRequestDTO requestDTO) {
        log.info("Received request to create cash movement");
        CashMovementResponseDTO response = cashMovementService.createCashMovement(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a cash movement by ID")
    public ResponseEntity<CashMovementResponseDTO> getCashMovementById(@PathVariable UUID id) {
        log.info("Received request to get cash movement with id: {}", id);
        return ResponseEntity.ok(cashMovementService.getCashMovementById(id));
    }

    @GetMapping("/session/{sessionId}")
    @Operation(summary = "Get all cash movements for a session")
    public ResponseEntity<List<CashMovementResponseDTO>> getCashMovementsBySession(
            @PathVariable UUID sessionId) {
        log.info("Received request to get cash movements for session: {}", sessionId);
        return ResponseEntity.ok(cashMovementService.getCashMovementsBySession(sessionId));
    }

    @GetMapping("/cashier/{cashierId}")
    @Operation(summary = "Get all cash movements for a cashier")
    public ResponseEntity<List<CashMovementResponseDTO>> getCashMovementsByCashier(
            @PathVariable UUID cashierId) {
        log.info("Received request to get cash movements for cashier: {}", cashierId);
        return ResponseEntity.ok(cashMovementService.getCashMovementsByCashier(cashierId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cash movement by ID")
    public ResponseEntity<Void> deleteCashMovement(@PathVariable UUID id) {
        log.info("Received request to delete cash movement with id: {}", id);
        cashMovementService.deleteCashMovement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all cash movements with pagination")
    public ResponseEntity<PaginatedResponseDTO<CashMovementResponseDTO>> getAllCashMovements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to get all cash movements (page: {}, size: {})", page, size);
        return ResponseEntity.ok(cashMovementService.getAllCashMovements(
                PageRequest.of(page, size)));
    }
}
