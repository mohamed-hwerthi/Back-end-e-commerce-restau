package com.foodsquad.FoodSquad.service.impl;


import com.foodsquad.FoodSquad.mapper.RoleMapper;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.RoleDTO;
import com.foodsquad.FoodSquad.model.entity.Role;
import com.foodsquad.FoodSquad.repository.RoleRepository;
import com.foodsquad.FoodSquad.service.declaration.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public PaginatedResponseDTO<RoleDTO> findAllRoles(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Role> rolesPage = roleRepository.findAll(pageable);
        
        List<RoleDTO> roleDTOs = rolesPage.getContent().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
                
        return new PaginatedResponseDTO<>(roleDTOs, rolesPage.getTotalElements());
    }

    @Override
    public List<RoleDTO> findAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO findRoleById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDTO findRoleByCode(String code) {
        Role role = roleRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with code: " + code));
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        // Check if role with the same code already exists
        if (roleRepository.existsByCode(roleDTO.getCode())) {
            throw new EntityNotFoundException("Role with code " + roleDTO.getCode() + " already exists");
        }
        
        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(String id, RoleDTO roleDTO) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        
        // Check if another role with the same code already exists
        if (roleDTO.getCode() != null && !existingRole.getCode().equals(roleDTO.getCode()) && 
            roleRepository.existsByCode(roleDTO.getCode())) {
            throw new EntityNotFoundException("Another role with code " + roleDTO.getCode() + " already exists");
        }
        
        roleMapper.updateRoleFromDto(roleDTO, existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public Role findRole(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> saveRoles(List<Role> roles) {
        return roleRepository.saveAll(roles);
    }
}
