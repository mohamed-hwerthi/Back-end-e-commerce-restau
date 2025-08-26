package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.EmployeeDTO;
import com.foodsquad.FoodSquad.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);
    
    EmployeeDTO toDto(Employee employee);
    
    @Mapping(target = "id", ignore = true)
    void updateEmployeeFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}
