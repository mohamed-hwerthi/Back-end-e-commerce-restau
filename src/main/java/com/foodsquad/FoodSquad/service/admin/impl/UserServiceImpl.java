package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.exception.UserAlreadyExistsException;
import com.foodsquad.FoodSquad.mapper.UserMapper;
import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.admin.dec.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link UserService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user with email {}", userDTO.getEmail());

        if (userRepository.existsByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber())) {
            log.error("User creation failed: email {} or phone {} already exists", userDTO.getEmail(), userDTO.getPhoneNumber());
            throw new UserAlreadyExistsException("Email or phone number is already in use");
        }

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        user = userRepository.save(user);
        log.info("User created with id {}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(UUID id) {
        log.info("Fetching user with id {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User not found with id {}", id);
                    return new RuntimeException("User not found");
                });
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        log.info("Updating user with id {}", id);

        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateEntityFromDto(userDTO, existing);
        userRepository.save(existing);

        UserDTO response = userMapper.toDto(existing);

        log.info("User updated successfully with id {}", id);
        return response;
    }


    @Override
    public User findById(UUID id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new EntityNotFoundException("User not found with id: " + id);
                });
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("Deleting user with id {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findByEmail(currentUsername)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + currentUsername));
    }
}
