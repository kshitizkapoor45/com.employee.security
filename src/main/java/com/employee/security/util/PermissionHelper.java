package com.employee.security.util;

import com.employee.security.model.Conste;
import com.employee.security.model.Permission;
import com.employee.security.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionHelper {

    private final PermissionRepository permissionRepository;

    public Permission getPermission(Permissions permissionEnum) {
        return permissionRepository.findByName(permissionEnum)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionEnum.name()));
    }

    public Set<Permission> getPermissionsForRole(Conste role) {
        Set<Permission> permissions = new HashSet<>();

        switch (role) {
            case ADMIN -> permissions.addAll(permissionRepository.findAll()); // full access
            case MANAGER -> {
                permissions.add(getPermission(Permissions.VIEW_EMPLOYEE));
                permissions.add(getPermission(Permissions.EDIT_EMPLOYEE));
                permissions.add(getPermission(Permissions.VIEW_DEPARTMENT));
            }
            case USER -> {
                permissions.add(getPermission(Permissions.VIEW_EMPLOYEE));
            }
            default -> log.warn("No permissions defined for role {}", role.name());
        }
        return permissions;
    }
}
