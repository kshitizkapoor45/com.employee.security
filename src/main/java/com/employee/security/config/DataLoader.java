package com.employee.security.config;

import com.employee.security.model.Conste;
import com.employee.security.model.Employee;
import com.employee.security.model.Permission;
import com.employee.security.model.Role;
import com.employee.security.repository.EmployeeRepo;
import com.employee.security.repository.PermissionRepository;
import com.employee.security.repository.RoleRepository;
import com.employee.security.util.PermissionConverter;
import com.employee.security.util.PermissionHelper;
import com.employee.security.util.Permissions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepo employeeRepo;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionHelper permissionHelper;

    @Override
    public void run(String... args) throws Exception {
        savePermission();
        saveRole();
        addAdmin();
    }

    public void addAdmin(){
        Role role = roleRepository.findByRole(Conste.ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
        boolean isExist = employeeRepo.existsByRole(role);
        if(!isExist){
            Employee employee = Employee.builder()
                    .name("admin")
                    .email("admin")
                    .role(role)
                    .password(passwordEncoder.encode("admin"))
                    .build();
            employeeRepo.save(employee);
            log.info("Admin created");
        }else{
            log.info("Admin already exists");
        }
    }

    public void savePermission(){
        addPermissions(Permissions.CREATE_DEPARTMENT);
        addPermissions(Permissions.DELETE_DEPARTMENT);
        addPermissions(Permissions.EDIT_DEPARTMENT);
        addPermissions(Permissions.VIEW_DEPARTMENT);
        addPermissions(Permissions.CREATE_EMPLOYEE);
        addPermissions(Permissions.DELETE_EMPLOYEE);
        addPermissions(Permissions.EDIT_EMPLOYEE);
        addPermissions(Permissions.VIEW_EMPLOYEE);
    }

    public void addPermissions(Permissions permission){
        boolean isExist = permissionRepository.existsByName(permission);
        if(!isExist){
            Permission newPermission = Permission.builder()
                    .name(permission)
                    .description(permission.getDescription())
                    .build();
            permissionRepository.save(newPermission);
            log.info(permission+" saved successfully");
        }else{
            log.info(permission+" already exists");
        }
    }

    private void saveRole(){
        addRole(Conste.ADMIN);
        addRole(Conste.USER);
        addRole(Conste.MANAGER);
    }

    private void addRole(Conste role){
        boolean isExists = roleRepository.existsByRole(role);
        if(!isExists){
            log.info("Adding Role {}",role.name());
            Set<Permission> permissions = permissionHelper.getPermissionsForRole(role);
            log.info("Permissions {}",permissions.size());

            Role r = Role.builder()
                    .name(role.getDescription())
                    .role(role)
                    .permissions(permissions)
                    .build();
            roleRepository.save(r);
        }else{
            log.info(role.name()+" already exists");
        }
    }
}
