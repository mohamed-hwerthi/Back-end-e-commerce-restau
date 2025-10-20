package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionResponseDTO;
import com.foodsquad.FoodSquad.service.CashierSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cashier-sessions")
@RequiredArgsConstructor
@Tag(name = "Cashier Session Management", description = "APIs for managing cashier sessions")
public class CashierSessionController {

    private final CashierSessionService cashierSessionService;

    @PostMapping
    @Operation(summary = "Create a new cashier session")
    public ResponseEntity<CashierSessionResponseDTO> createSession(
            @Valid @RequestBody CashierSessionRequestDTO requestDTO) {
        CashierSessionResponseDTO response = cashierSessionService.createSession(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing cashier session")
    public ResponseEntity<CashierSessionResponseDTO> updateSession(
            @PathVariable UUID id,
            @Valid @RequestBody CashierSessionRequestDTO requestDTO) {
        CashierSessionResponseDTO response = cashierSessionService.updateSession(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/close")
    @Operation(summary = "Close a cashier session")
    public ResponseEntity<CashierSessionResponseDTO> closeSession(
            @PathVariable UUID id,
            @RequestParam Double closingBalance) {
        CashierSessionResponseDTO response = cashierSessionService.closeSession(id, closingBalance);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a cashier session by ID")
    public ResponseEntity<CashierSessionResponseDTO> getSessionById(@PathVariable UUID id) {
        return ResponseEntity.ok(cashierSessionService.getSessionById(id));
    }

    @GetMapping
    @Operation(summary = "Get all cashier sessions")
    public ResponseEntity<List<CashierSessionResponseDTO>> getAllSessions() {
        return ResponseEntity.ok(cashierSessionService.getAllSessions());
    }

    @GetMapping("/cashier/{cashierId}")
    @Operation(summary = "Get all sessions for a specific cashier")
    public ResponseEntity<List<CashierSessionResponseDTO>> getSessionsByCashier(
            @PathVariable UUID cashierId) {
        return ResponseEntity.ok(cashierSessionService.getSessionsByCashier(cashierId));
    }

    @GetMapping("/status/{isClosed}")
    @Operation(summary = "Get all sessions by status (open/closed)")
    public ResponseEntity<List<CashierSessionResponseDTO>> getSessionsByStatus(
            @PathVariable Boolean isClosed) {
        return ResponseEntity.ok(cashierSessionService.getSessionsByStatus(isClosed));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cashier session")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        cashierSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
