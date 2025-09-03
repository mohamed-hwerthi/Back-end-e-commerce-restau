package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.exception.UserAlreadyExistsException;
import com.foodsquad.FoodSquad.exception.InvalidCredentialsException;
import com.foodsquad.FoodSquad.model.dto.*;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.StoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    private final StoreService storeService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = findUserByEmail(email);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

    public User loadUserEntityByUsername(String email) {
        return findUserByEmail(email);
    }

    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        checkUserExists(registrationDTO.getEmail());

        User newUser = User.builder()
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(UserRole.EMPLOYEE)
                .build();

        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Transactional
    public StoreOwnerAuthResponse loginStoreOwner(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        StoreDTO storeDto = storeService.findByOwner(user);

        return StoreOwnerAuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .storeId(storeDto.getId())
                .build();
    }




    @Transactional
    public UserResponseDTO loginCashier(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.CASHIER) {
            throw new InvalidCredentialsException("User is not authorized as an admin or cashier");
        }

        return modelMapper.map(user, UserResponseDTO.class);
    }


    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private void checkUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists");
        }
    }
}
