package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findById(String id);

    boolean existsById(String id);

}
