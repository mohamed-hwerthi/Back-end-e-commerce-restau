package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.EmployeeDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    PaginatedResponseDTO<EmployeeDTO> findAllEmployees(int page, int limit);

    List<EmployeeDTO> findAllEmployees();

    EmployeeDTO findEmployeeById(UUID id);

    EmployeeDTO findEmployeeByEmployeeId(UUID employeeId);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO);

    void deleteEmployee(UUID id);

    Employee findEmployee(UUID id);

    Employee saveEmployee(Employee employee);

}
