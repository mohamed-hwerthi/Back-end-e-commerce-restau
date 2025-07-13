package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.dto.UserLoginDTO;
import com.foodsquad.FoodSquad.model.dto.UserRegistrationDTO;
import com.foodsquad.FoodSquad.model.dto.UserResponseDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.TokenRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private  final UserRepository userRepository;
    private   final PasswordEncoder passwordEncoder;
    private  final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    public User loadUserEntityByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userRegistrationDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRole(UserRole.NORMAL);

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO loginUser(UserLoginDTO userLoginDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userLoginDTO.getEmail());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return modelMapper.map(user, UserResponseDTO.class);
    }
}
