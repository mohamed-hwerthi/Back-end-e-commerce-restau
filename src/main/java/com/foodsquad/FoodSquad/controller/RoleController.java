package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.RoleDTO;
import com.foodsquad.FoodSquad.service.declaration.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<RoleDTO>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(roleService.findAllRoles(page, limit));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDTO>> getAllRolesList() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable String id) {
        return ResponseEntity.ok(roleService.findRoleById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<RoleDTO> getRoleByCode(@PathVariable String code) {
        return ResponseEntity.ok(roleService.findRoleByCode(code));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable String id,
            @Valid @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
