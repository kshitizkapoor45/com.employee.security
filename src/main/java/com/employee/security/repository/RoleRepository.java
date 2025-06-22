package com.employee.security.repository;

import com.employee.security.model.Conste;
import com.employee.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
     boolean existsByRole(Conste role);
     Optional<Role> findByRole(Conste role);
}
