package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.RoleDTO;
import com.foodsquad.FoodSquad.model.entity.Role;

import java.util.List;

public interface RoleService {

    PaginatedResponseDTO<RoleDTO> findAllRoles(int page, int limit);

    List<RoleDTO> findAllRoles();

    RoleDTO findRoleById(String id);

    RoleDTO findRoleByCode(String code);

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(String id, RoleDTO roleDTO);

    void deleteRole(String id);

    Role findRole(String id);

    Role saveRole(Role role);

    List<Role> saveRoles(List<Role> roles);

}
