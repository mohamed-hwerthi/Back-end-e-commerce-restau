package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.EmployeeDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Employee;

import java.util.List;

public interface EmployeeService {

    PaginatedResponseDTO<EmployeeDTO> findAllEmployees(int page, int limit);

    List<EmployeeDTO> findAllEmployees();

    EmployeeDTO findEmployeeById(String id);

    EmployeeDTO findEmployeeByEmployeeId(String employeeId);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(String id, EmployeeDTO employeeDTO);

    void deleteEmployee(String id);

    Employee findEmployee(String id);

    Employee saveEmployee(Employee employee);

}
