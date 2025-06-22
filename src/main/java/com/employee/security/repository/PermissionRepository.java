package com.employee.security.repository;

import com.employee.security.model.Conste;
import com.employee.security.model.Permission;
import com.employee.security.util.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {
    boolean existsByName(Permissions name);
    Optional<Permission> findByName(Permissions name);
}
