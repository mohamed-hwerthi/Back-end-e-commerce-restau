package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.AdminDTO;
import com.foodsquad.FoodSquad.model.entity.Admin;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {

    @Mapping(target = "id", ignore = true)
    Admin toEntity(AdminDTO adminDTO);

    AdminDTO toDto(Admin admin);

    @Mapping(target = "id", ignore = true)
    void updateAdminFromDto(AdminDTO adminDTO, @MappingTarget Admin admin);
}
