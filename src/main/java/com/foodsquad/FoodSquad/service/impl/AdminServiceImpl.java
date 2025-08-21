package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.AdminMapper;
import com.foodsquad.FoodSquad.mapper.UserMapper;
import com.foodsquad.FoodSquad.model.dto.AdminDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.entity.Admin;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.AdminRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final AdminMapper adminMapper;

    private final UserMapper userMapper;

    private  final UserRepository userRepository ;

    private static  final String ADMIN_NOT_FOUND = "Admin not found with id: ";

    @Override
    public PaginatedResponseDTO<AdminDTO> findAllAdmins(int page, int limit) {

        Pageable pageable = PageRequest.of(page, limit);
        Page<Admin> adminsPage = adminRepository.findAll(pageable);

        List<AdminDTO> adminDTOs = adminsPage.getContent().stream()
                .map(adminMapper::toDto)
                .toList();

        return new PaginatedResponseDTO<>(adminDTOs, adminsPage.getTotalElements());
    }

    @Override
    public List<AdminDTO> findAllAdmins() {

        return adminRepository.findAll().stream()
                .map(adminMapper::toDto)
                .toList();
    }

    @Override
    public AdminDTO findAdminById(String id) {

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADMIN_NOT_FOUND + id));
        return adminMapper.toDto(admin);
    }

    @Override
    public AdminDTO findAdminByAdminId(String adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with admin ID: " + adminId));
        return adminMapper.toDto(admin);
    }

    @Override
    @Transactional
    public AdminDTO createAdmin(AdminDTO adminDTO) {

        if (adminRepository.existsById(adminDTO.getId())) {
            throw new EntityNotFoundException("Admin with ID " + adminDTO.getId() + " already exists");
        }
        Admin admin = adminMapper.toEntity(adminDTO);
        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    @Transactional
    public AdminDTO updateAdmin(String id, AdminDTO adminDTO) {

        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADMIN_NOT_FOUND + id));

        adminMapper.updateAdminFromDto(adminDTO, existingAdmin);
        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return adminMapper.toDto(updatedAdmin);
    }

    @Override
    @Transactional
    public void deleteAdmin(String id) {

        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException(ADMIN_NOT_FOUND + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    public Admin findAdmin(String id) {

        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADMIN_NOT_FOUND + id));
    }

    @Override
    public Admin saveAdmin(Admin admin) {

        return adminRepository.save(admin);
    }

    @Override
    public User createStoreOwner(UserDTO userDTO) {
            User user = userMapper.toEntity(userDTO);
            user.setRole(UserRole.OWNER);

            return  userRepository.save(user);
        }

}
