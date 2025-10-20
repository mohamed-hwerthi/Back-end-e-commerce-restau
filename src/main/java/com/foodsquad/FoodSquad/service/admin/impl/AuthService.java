package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.config.security.EncryptionUtil;
import com.foodsquad.FoodSquad.exception.InvalidCredentialsException;
import com.foodsquad.FoodSquad.exception.UserAlreadyExistsException;
import com.foodsquad.FoodSquad.model.dto.AuthResponseDTO;
import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.model.dto.UserLoginDTO;
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

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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


    /**
     * ✅ Unified Sign-In Method for All Users
     */
    @Transactional
    public AuthResponseDTO signIn(UserLoginDTO loginDTO) {
        User user = getUserIfPasswordMatches(loginDTO);

        AuthResponseDTO.AuthResponseDTOBuilder response = AuthResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole());

        if (user.getRole() == UserRole.OWNER) {
            StoreDTO storeDto = storeService.findByOwner(user);
            String encryptedStoreId = encryptStoreId(storeDto.getId().toString());
            response.storeId(encryptedStoreId);
        }

        return response.build();
    }

    /**
     * ✅ Build JWT claims map
     */
    public Map<String, Object> buildClaims(AuthResponseDTO authResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authResponse.getId());
        claims.put("email", authResponse.getEmail());
        claims.put("role", authResponse.getRole());

        if (authResponse.getStoreId() != null) {
            claims.put("storeId", authResponse.getStoreId());
        }

        return claims;
    }

    // ------------------- PRIVATE HELPERS -------------------

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private void checkUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists");
        }
    }

    private User getUserIfPasswordMatches(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }

    private String encryptStoreId(String storeId) {
        try {
            return EncryptionUtil.encrypt(storeId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt store ID", e);
        }
    }
}
