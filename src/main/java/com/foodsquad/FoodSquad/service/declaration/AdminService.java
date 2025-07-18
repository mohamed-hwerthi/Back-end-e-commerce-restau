package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.AdminDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.entity.Admin;
import com.foodsquad.FoodSquad.model.entity.User;

import java.util.List;

public interface AdminService {

    PaginatedResponseDTO<AdminDTO> findAllAdmins(int page, int limit);

    List<AdminDTO> findAllAdmins();

    AdminDTO findAdminById(String id);

    AdminDTO findAdminByAdminId(String adminId);

    AdminDTO createAdmin(AdminDTO adminDTO);

    AdminDTO updateAdmin(String id, AdminDTO adminDTO);

    void deleteAdmin(String id);

    Admin findAdmin(String id);

    Admin saveAdmin(Admin admin);

    User createStoreOwner(UserDTO userDTO);

}
