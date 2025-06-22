package com.employee.security.repository;

import com.employee.security.model.Conste;
import com.employee.security.model.Employee;
import com.employee.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    Optional<Employee> findByEmail(String email);
    boolean existsByRole(Role role);
}
