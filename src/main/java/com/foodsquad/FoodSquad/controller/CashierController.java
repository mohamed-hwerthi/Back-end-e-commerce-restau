package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CashierDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.service.declaration.CashierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cashiers")
public class CashierController {

    private final CashierService cashierService;

    @Autowired
    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<CashierDTO>> getAllCashiers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(cashierService.findAllCashiers(page, limit));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CashierDTO>> getAllCashiersList() {
        return ResponseEntity.ok(cashierService.findAllCashiers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashierDTO> getCashierById(@PathVariable UUID id ) {
        return ResponseEntity.ok(cashierService.findCashierById(id));
    }

    @GetMapping("/cashier-id/{cashierId}")
    public ResponseEntity<CashierDTO> getCashierByCashierId(@PathVariable UUID cashierId) {
        return ResponseEntity.ok(cashierService.findCashierByCashierId(cashierId));
    }

    @PostMapping
    public ResponseEntity<CashierDTO> createCashier(@Valid @RequestBody CashierDTO cashierDTO) {
        CashierDTO createdCashier = cashierService.createCashier(cashierDTO);
        return new ResponseEntity<>(createdCashier, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CashierDTO> updateCashier(
            @PathVariable UUID id,
            @Valid @RequestBody CashierDTO cashierDTO) {
        return ResponseEntity.ok(cashierService.updateCashier(id, cashierDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashier(@PathVariable UUID id) {
        cashierService.deleteCashier(id);
        return ResponseEntity.noContent().build();
    }
}
