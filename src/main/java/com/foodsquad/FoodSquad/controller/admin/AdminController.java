package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.AdminDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.service.admin.dec.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<AdminDTO>> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(adminService.findAllAdmins(page, limit));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdminDTO>> getAllAdminsList() {
        return ResponseEntity.ok(adminService.findAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.findAdminById(id));
    }

    @GetMapping("/admin-id/{adminId}")
    public ResponseEntity<AdminDTO> getAdminByAdminId(@PathVariable UUID adminId) {
        return ResponseEntity.ok(adminService.findAdminByAdminId(adminId));
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        AdminDTO createdAdmin = adminService.createAdmin(adminDTO);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(
            @PathVariable UUID id,
            @Valid @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/store-owner")
    @Operation(
            summary = "Create a new store owner",
            description = "Creates a new store owner with email and phone number"
    )
    public ResponseEntity<User> createStoreOwner(@RequestBody UserDTO userDto) {
        log.info("Received request to create store owner for email: {}", userDto.getEmail());

        User createdUser = adminService.createStoreOwner(userDto.getEmail(),
                userDto.getPhoneNumber(),
                userDto.getPhoneNumber());
        log.info("Store owner created with id: {}", createdUser.getId());

        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/by-email")
    @Operation(
            summary = "Find user by email",
            description = "Retrieves a user by their email address"
    )
    public ResponseEntity<UserDTO> findByEmail(@RequestParam String email) {
        log.info("Received request to find user by email: {}", email);

        User user = adminService.findByEmail(email);
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();

        log.info("Found user with ID: {}", user.getId());
        return ResponseEntity.ok(userDTO);
    }

}
