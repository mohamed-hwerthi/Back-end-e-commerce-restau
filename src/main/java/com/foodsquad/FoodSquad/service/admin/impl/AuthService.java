package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.config.security.EncryptionUtil;
import com.foodsquad.FoodSquad.exception.InvalidCredentialsException;
import com.foodsquad.FoodSquad.exception.UserAlreadyExistsException;
import com.foodsquad.FoodSquad.mapper.UserMapper;
import com.foodsquad.FoodSquad.model.dto.*;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.admin.dec.StoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreService storeService;
    private final UserMapper userMapper;

    /**
     * Spring Security calls this method to load a user by email.
     * Store owners always reside in the public schema.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = findUserByEmail(email);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

    /**
     * Load the actual User entity by email
     */
    public User loadUserEntityByUsername(String email) {
        return findUserByEmail(email);
    }

    /**
     * Register a new employee
     */
    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        checkUserExists(registrationDTO.getEmail());

        User newUser = User.builder()
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(UserRole.EMPLOYEE)
                .build();

        return userMapper.toResponseDto(userRepository.save(newUser));
    }

    /**
     * Login for general users (employees)
     */
    @Transactional
    public UserResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = getUserIfPasswordMatches(loginDTO);
        return userMapper.toResponseDto(user);
    }

    /**
     * Login for store owners
     */
    @Transactional
    public StoreOwnerAuthResponse loginStoreOwner(UserLoginDTO loginDTO) {
        User user = getUserIfPasswordMatches(loginDTO);
        StoreDTO storeDto = storeService.findByOwner(user);
        String encryptedStoreId = encryptStoreId(storeDto.getId().toString());

        return StoreOwnerAuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .storeId(encryptedStoreId)
                .build();
    }

    /**
     * Login for admin or cashier users
     */
    @Transactional
    public UserResponseDTO loginCashier(UserLoginDTO loginDTO) {
        User user = getUserIfPasswordMatches(loginDTO);

        if (!isAdminOrCashier(user)) {
            throw new InvalidCredentialsException("User is not authorized as an admin or cashier");
        }

        return userMapper.toResponseDto(user);
    }

    // ------------------- PRIVATE HELPERS -------------------

    /**
     * Find user by email or throw exception
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Check if user email already exists
     */
    private void checkUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists");
        }
    }

    /**
     * Verify password matches or throw exception
     */
    private User getUserIfPasswordMatches(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return user;
    }

    /**
     * Check if the user role is ADMIN or CASHIER
     */
    private boolean isAdminOrCashier(User user) {
        UserRole role = user.getRole();
        return role == UserRole.ADMIN || role == UserRole.CASHIER;
    }

    /**
     * Encrypt store ID
     */
    private String encryptStoreId(String storeId) {
        try {
            return EncryptionUtil.encrypt(storeId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt store ID", e);
        }
    }
}
