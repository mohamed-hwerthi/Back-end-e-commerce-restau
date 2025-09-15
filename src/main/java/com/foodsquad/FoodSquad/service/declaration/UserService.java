package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.UserResponseDTO;
import com.foodsquad.FoodSquad.model.dto.UserUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {

    /**
     * Returns a paginated list of users with their order counts.
     *
     * @param page the page number (0-based)
     * @param size the number of items per page
     * @return list of UserResponseDTO
     */
    List<UserResponseDTO> getAllUsers(int page, int size);

    /**
     * Returns a user by ID, including order count.
     *
     * @param id user ID
     * @return ResponseEntity with UserResponseDTO
     */
    ResponseEntity<UserResponseDTO> getUserById(UUID id);

    /**
     * Updates a user's details.
     *
     * @param id            user ID
     * @param userUpdateDTO update data
     * @return ResponseEntity with updated UserResponseDTO
     */
    ResponseEntity<UserResponseDTO> updateUser(UUID id, UserUpdateDTO userUpdateDTO);

    /**
     * Deletes a user by ID.
     *
     * @param id user ID
     * @return ResponseEntity with success message
     */
    ResponseEntity<Map<String, String>> deleteUser(UUID id);
}