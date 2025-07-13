package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.AdminMapper;
import com.foodsquad.FoodSquad.model.dto.AdminDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Admin;
import com.foodsquad.FoodSquad.repository.AdminRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
        return adminMapper.toDto(admin);
    }

    @Override
    public AdminDTO findAdminByAdminId(String adminId) {
        Admin admin = adminRepository.findByAdminId(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with admin ID: " + adminId));
        return adminMapper.toDto(admin);
    }

    @Override
    @Transactional
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        if (adminRepository.existsByAdminId(adminDTO.getAdminId())) {
            throw new EntityNotFoundException("Admin with ID " + adminDTO.getAdminId() + " already exists");
        }
        Admin admin = adminMapper.toEntity(adminDTO);
        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    @Transactional
    public AdminDTO updateAdmin(String id, AdminDTO adminDTO) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
        
        adminMapper.updateAdminFromDto(adminDTO, existingAdmin);
        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return adminMapper.toDto(updatedAdmin);
    }

    @Override
    @Transactional
    public void deleteAdmin(String id) {
        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    public Admin findAdmin(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
}
