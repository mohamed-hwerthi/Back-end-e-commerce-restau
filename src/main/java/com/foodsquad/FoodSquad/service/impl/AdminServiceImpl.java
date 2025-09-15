package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.exception.UserAlreadyExistsException;
import com.foodsquad.FoodSquad.mapper.AdminMapper;
import com.foodsquad.FoodSquad.mapper.UserMapper;
import com.foodsquad.FoodSquad.model.dto.AdminDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Admin;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.AdminRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private static final String ADMIN_NOT_FOUND = "Admin not found with id: ";
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    @Override
    public PaginatedResponseDTO<AdminDTO> findAllAdmins(int page, int limit) {
        log.info("Fetching all admins - page: {}, limit: {}", page, limit);
        Pageable pageable = PageRequest.of(page, limit);
        Page<Admin> adminsPage = adminRepository.findAll(pageable);
        List<AdminDTO> adminDTOs = adminsPage.getContent().stream()
                .map(adminMapper::toDto)
                .toList();
        log.info("Found {} admins", adminsPage.getTotalElements());
        return new PaginatedResponseDTO<>(adminDTOs, adminsPage.getTotalElements());
    }

    @Override
    public List<AdminDTO> findAllAdmins() {
        log.info("Fetching all admins");
        List<AdminDTO> admins = adminRepository.findAll().stream()
                .map(adminMapper::toDto)
                .toList();
        log.info("Found {} admins", admins.size());
        return admins;
    }

    @Override
    public AdminDTO findAdminById(UUID id) {
        log.info("Fetching admin by ID: {}", id);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADMIN_NOT_FOUND + id));
        return adminMapper.toDto(admin);
    }

    @Override
    public AdminDTO findAdminByAdminId(UUID adminId) {
        log.info("Fetching admin by Admin ID: {}", adminId);
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with admin ID: " + adminId));
        return adminMapper.toDto(admin);
    }

    @Override
    @Transactional
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        log.info("Creating new admin: {}", adminDTO);
        if (adminDTO.getId() != null && adminRepository.existsById(adminDTO.getId())) {
            log.error("Admin with ID {} already exists", adminDTO.getId());
            throw new EntityNotFoundException("Admin with ID " + adminDTO.getId() + " already exists");
        }
        Admin admin = adminMapper.toEntity(adminDTO);
        Admin savedAdmin = adminRepository.save(admin);
        log.info("Created admin with ID: {}", savedAdmin.getId());
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    @Transactional
    public AdminDTO updateAdmin(UUID id, AdminDTO adminDTO) {
        log.info("Updating admin with ID: {}", id);
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADMIN_NOT_FOUND + id));
        adminMapper.updateAdminFromDto(adminDTO, existingAdmin);
        Admin updatedAdmin = adminRepository.save(existingAdmin);
        log.info("Updated admin with ID: {}", updatedAdmin.getId());
        return adminMapper.toDto(updatedAdmin);
    }

    @Override
    @Transactional
    public void deleteAdmin(UUID id) {
        log.info("Deleting admin with ID: {}", id);
        if (!adminRepository.existsById(id)) {
            log.error("Cannot delete. Admin with ID {} not found", id);
            throw new EntityNotFoundException(ADMIN_NOT_FOUND + id);
        }
        adminRepository.deleteById(id);
        log.info("Deleted admin with ID: {}", id);
    }

    @Override
    public Admin findAdmin(UUID id) {
        log.info("Finding admin entity by ID: {}", id);
        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADMIN_NOT_FOUND + id));
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        log.info("Saving admin entity: {}", admin);
        return adminRepository.save(admin);
    }


    @Override
    @Transactional
    public User createStoreOwner(String email, String phoneNumber, String password) {
        checkUserExists(email);
        log.info("Creating store owner with email: {} and phone: {}", email, phoneNumber);
        User storeOwner = User.builder()
                .email(email)
                .phoneNumber(phoneNumber)
                .password(passwordEncoder.encode(password))
                .role(UserRole.OWNER)
                .build();
        User savedUser = userRepository.save(storeOwner);
        log.info("Successfully created store owner with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }


    private void checkUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            log.error("Email {} already exists", email);
            throw new UserAlreadyExistsException("Email already exists");
        }
    }
}
