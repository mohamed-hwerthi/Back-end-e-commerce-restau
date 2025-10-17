package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.service.admin.dec.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing users.
 * Provides endpoints for creating, reading, updating, and deleting users.
 */
@Tag(name = "User Management" , description = "User Management Endpoints")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Create a new user.
     *
     * @param userDTO the user DTO containing user data
     * @return the created user DTO
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("Request received to create user with email: {}", userDTO.getEmail());
        UserDTO createdUser = userService.createUser(userDTO);
        log.info("User created successfully with ID: {}", createdUser.getId());
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Get all users.
     *
     * @return a list of all users as DTOs
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Request received to fetch all users");
        List<UserDTO> users = userService.getAllUsers();
        log.info("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * Get a user by ID.
     *
     * @param id the UUID of the user
     * @return the user DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        log.info("Request received to fetch user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
        log.info("User fetched successfully with ID: {}", id);
        return ResponseEntity.ok(user);
    }

    /**
     * Update an existing user.
     *
     * @param id      the UUID of the user to update
     * @param userDTO the updated user data
     * @return the updated user DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        log.info("Request received to update user with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        log.info("User updated successfully with ID: {}", id);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete a user by ID.
     *
     * @param id the UUID of the user to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Request received to delete user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}

