package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.RoleDTO;
import com.foodsquad.FoodSquad.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDto(Role role);

    List<RoleDTO> toDtoList(List<Role> roles);

    void updateRoleFromDto(RoleDTO roleDTO, @MappingTarget Role role);

}
