package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Create a new user.
     *
     * @param userDTO User data transfer object.
     * @return Created UserDTO without password.
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * Get all users.
     *
     * @return List of all users as DTOs.
     */
    List<UserDTO> getAllUsers();

    /**
     * Get user by ID.
     *
     * @param id UUID of the user.
     * @return UserDTO if found.
     */
    UserDTO getUserById(UUID id);

    /**
     * Update an existing user.
     *
     * @param id UUID of the user to update.
     * @param userDTO Updated user DTO.
     * @return Updated UserDTO.
     */
    UserDTO updateUser(UUID id, UserDTO userDTO);

    /**
     * Delete a user by ID.
     *
     * @param id UUID of the user to delete.
     */
    void deleteUser(UUID id);

    User findById(UUID id) ;


}