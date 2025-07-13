package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.dto.UserResponseDTO;
import com.foodsquad.FoodSquad.model.dto.UserUpdateDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {

    private final  UserRepository userRepository;

    private  final  ModelMapper modelMapper;


    private  final  OrderRepository orderRepository;

    private User getCurrentUser() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void checkOwnership(String userId) {

        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals(UserRole.ADMIN) && !currentUser.getRole().equals(UserRole.MODERATOR)) {
            throw new IllegalArgumentException("Access denied");
        }
    }

   @Override
    public List<UserResponseDTO> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdOn"));
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.stream()
                .map(user -> {
                    long ordersCount = orderRepository.countByUserId(user.getId());
                    UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
                    userResponseDTO.setOrdersCount(ordersCount);
                    return userResponseDTO;
                })
                .toList();
    }
  @Override
    public ResponseEntity<UserResponseDTO> getUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found for ID: " + id));
        long ordersCount = orderRepository.countByUserId(id);
        UserResponseDTO userDTO = modelMapper.map(user, UserResponseDTO.class);
        userDTO.setOrdersCount(ordersCount);
        return ResponseEntity.ok(userDTO);
    }

    @Transactional
    public ResponseEntity<UserResponseDTO> updateUser(String id, UserUpdateDTO userUpdateDTO) {

        User currentUser = getCurrentUser();
        checkOwnership(id);
        if (currentUser.getId().equals(id) && !currentUser.getRole().name().equals(userUpdateDTO.getRole())) {
            throw new IllegalArgumentException("Users cannot update their own role.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found for ID: " + id));


        if (currentUser.getRole().equals(UserRole.NORMAL) && !userUpdateDTO.getRole().equals(UserRole.NORMAL.name())) {
            throw new IllegalArgumentException("Normal users cannot change roles.");
        }

        // Check if the current user is not an admin but is trying to set the role to admin
        if (!currentUser.getRole().equals(UserRole.ADMIN) && userUpdateDTO.getRole().equals(UserRole.ADMIN.name())) {
            throw new IllegalArgumentException("Only admin users can assign the admin role.");
        }

        // Check if the user role is ADMIN and is being changed
        if (user.getRole() == UserRole.ADMIN && !userUpdateDTO.getRole().equals(UserRole.ADMIN.name())) {
            throw new IllegalArgumentException("Admin user role cannot be changed");
        }

        user.setName(userUpdateDTO.getName());
        user.setRole(UserRole.valueOf(userUpdateDTO.getRole()));
        user.setImageUrl(userUpdateDTO.getImageUrl());
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());

        userRepository.save(user);
        long ordersCount = orderRepository.countByUserId(id);
        UserResponseDTO updatedUserDTO = modelMapper.map(user, UserResponseDTO.class);
        updatedUserDTO.setOrdersCount(ordersCount);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteUser(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found for ID: " + id));
        if (user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("Admin users cannot be deleted.");
        }

        user.getMenuItems().forEach(menuItem -> {
            orderRepository.removeMenuItemReferences(menuItem.getId());
        });

        userRepository.delete(user);
        return ResponseEntity.ok(Map.of("message", "User successfully deleted."));
    }

}
