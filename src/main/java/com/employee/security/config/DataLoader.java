package com.employee.security.config;

import com.employee.security.model.Conste;
import com.employee.security.model.Employee;
import com.employee.security.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        addAdmin();
    }

    public void addAdmin(){
        boolean isExist = employeeRepo.existsByRole(Conste.ADMIN);
        if(!isExist){
            Employee employee = Employee.builder()
                    .name("admin")
                    .email("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Conste.ADMIN)
                    .build();
            employeeRepo.save(employee);
            log.info("Admin created");
        }else{
            log.info("Admin already exists");
        }
    }
}
