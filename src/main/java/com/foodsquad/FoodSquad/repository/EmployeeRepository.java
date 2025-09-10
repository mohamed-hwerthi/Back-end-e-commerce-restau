package com.foodsquad.FoodSquad.repository;

import com.foodsquad.FoodSquad.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findById(UUID id);

    boolean existsById(UUID id);

}
