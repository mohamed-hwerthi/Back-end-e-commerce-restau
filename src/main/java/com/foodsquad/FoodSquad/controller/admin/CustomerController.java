package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.CustomerDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.service.admin.dec.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing customers.
 * Provides CRUD operations for customer management.
 */
@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer",
               description = "Creates a new customer with the provided details")
    @ApiResponse(responseCode = "201", description = "Customer created successfully",
                content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<CustomerDTO> createCustomer(
            @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Received request to create a new customer");
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID",
               description = "Retrieves customer details by their unique identifier")
    @ApiResponse(responseCode = "200", description = "Customer found",
                content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "ID of the customer to be retrieved") 
            @PathVariable UUID id) {
        log.debug("Fetching customer with ID: {}", id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping
    @Operation(summary = "Get all customers",
               description = "Retrieves a paginated list of all customers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customers")
    public ResponseEntity<PaginatedResponseDTO<CustomerDTO>> getAllCustomers(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Fetching all customers with pagination");
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer",
               description = "Updates an existing customer's details")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to be updated")
            @PathVariable UUID id,
            @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Updating customer with ID: {}", id);
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer",
               description = "Deletes a customer by their ID")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to be deleted")
            @PathVariable UUID id) {
        log.info("Deleting customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
