package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.CashierDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.service.admin.dec.CashierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing {@link com.foodsquad.FoodSquad.model.dto.CashierDTO}.
 * Provides CRUD operations and paginated retrieval of cashiers.
 *
 * <p>This controller is part of the admin module and should be accessed
 * only by authorized administrative users.</p>
 */
@RestController
@RequestMapping("/api/cashiers")
@Tag(name = "Cashier Management", description = "APIs for managing cashiers within the FoodSquad application")
public class CashierController {

    private final CashierService cashierService;

    @Autowired
    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    /**
     * Retrieves a paginated list of all cashiers.
     *
     * @param page  the page number to retrieve (default: 0)
     * @param limit the number of records per page (default: 10)
     * @return paginated list of {@link CashierDTO}
     */
    @Operation(summary = "Get paginated list of cashiers", description = "Retrieve a paginated list of all cashiers with page and limit parameters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PaginatedResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<CashierDTO>> getAllCashiers(
            @Parameter(description = "Page number (zero-based index)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(cashierService.findAllCashiers(page, limit));
    }

    /**
     * Retrieves all cashiers without pagination.
     *
     * @return list of all {@link CashierDTO}
     */
    @Operation(summary = "Get all cashiers", description = "Retrieve a complete list of all cashiers without pagination.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CashierDTO.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<CashierDTO>> getAllCashiersList() {
        return ResponseEntity.ok(cashierService.findAllCashiers());
    }

    /**
     * Retrieves a cashier by its internal ID.
     *
     * @param id the UUID of the cashier
     * @return {@link CashierDTO} representing the cashier
     */
    @Operation(summary = "Get cashier by ID", description = "Retrieve a cashier's details using its internal UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cashier found",
                    content = @Content(schema = @Schema(implementation = CashierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cashier not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CashierDTO> getCashierById(
            @Parameter(description = "UUID of the cashier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(cashierService.findCashierById(id));
    }

    /**
     * Retrieves a cashier by its cashier-specific ID.
     *
     * @param cashierId the UUID of the cashier
     * @return {@link CashierDTO} representing the cashier
     */
    @Operation(summary = "Get cashier by cashier ID", description = "Retrieve a cashier's details using their cashier-specific UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cashier found",
                    content = @Content(schema = @Schema(implementation = CashierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cashier not found")
    })
    @GetMapping("/cashier-id/{cashierId}")
    public ResponseEntity<CashierDTO> getCashierByCashierId(
            @Parameter(description = "Cashier-specific UUID", required = true)
            @PathVariable UUID cashierId) {
        return ResponseEntity.ok(cashierService.findCashierByCashierId(cashierId));
    }

    /**
     * Creates a new cashier.
     *
     * @param cashierDTO the cashier data to create
     * @return created {@link CashierDTO}
     */
    @Operation(summary = "Create a new cashier", description = "Add a new cashier to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cashier created successfully",
                    content = @Content(schema = @Schema(implementation = CashierDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<CashierDTO> createCashier(
            @Valid @RequestBody CashierDTO cashierDTO) {
        CashierDTO createdCashier = cashierService.createCashier(cashierDTO);
        return new ResponseEntity<>(createdCashier, HttpStatus.CREATED);
    }

    /**
     * Updates an existing cashier.
     *
     * @param id          the UUID of the cashier to update
     * @param cashierDTO  updated cashier data
     * @return updated {@link CashierDTO}
     */
    @Operation(summary = "Update cashier", description = "Update existing cashier information.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cashier updated successfully",
                    content = @Content(schema = @Schema(implementation = CashierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cashier not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CashierDTO> updateCashier(
            @Parameter(description = "UUID of the cashier to update", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody CashierDTO cashierDTO) {
        return ResponseEntity.ok(cashierService.updateCashier(id, cashierDTO));
    }

    /**
     * Deletes a cashier by ID.
     *
     * @param id the UUID of the cashier to delete
     * @return HTTP 204 if successful
     */
    @Operation(summary = "Delete cashier", description = "Delete a cashier by their UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cashier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cashier not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashier(
            @Parameter(description = "UUID of the cashier to delete", required = true)
            @PathVariable UUID id) {
        cashierService.deleteCashier(id);
        return ResponseEntity.noContent().build();
    }
}
