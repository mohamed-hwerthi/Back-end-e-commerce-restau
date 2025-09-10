package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.AdminDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.UserDTO;
import com.foodsquad.FoodSquad.model.entity.Admin;
import com.foodsquad.FoodSquad.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    PaginatedResponseDTO<AdminDTO> findAllAdmins(int page, int limit);

    List<AdminDTO> findAllAdmins();

    AdminDTO findAdminById(UUID id);

    AdminDTO findAdminByAdminId(UUID adminId);

    AdminDTO createAdmin(AdminDTO adminDTO);

    AdminDTO updateAdmin(UUID  id, AdminDTO adminDTO);

    void deleteAdmin(UUID id);

    Admin findAdmin(UUID id);

    Admin saveAdmin(Admin admin);

    User createStoreOwner(String email, String phoneNumber, String password) ;

    User findByEmail(String email ) ;
}
