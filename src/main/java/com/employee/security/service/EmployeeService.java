package com.employee.security.service;

import com.employee.security.config.JwtService;
import com.employee.security.model.Conste;
import com.employee.security.model.Employee;
import com.employee.security.model.EmployeeRequest;
import com.employee.security.model.Role;
import com.employee.security.repository.RoleRepository;
import com.employee.security.util.LoginRequest;
import com.employee.security.util.Response;
import com.employee.security.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    public Employee createEmployee(EmployeeRequest employee){
        String password = passwordEncoder.encode(employee.getPassword());
        Role role = roleRepository.findByRole(employee.getRole()).orElseThrow(() -> new RuntimeException("Role not found"));
        Employee emp = Employee.builder()
                .name(employee.getName())
                .password(password)
                .email(employee.getEmail())
                .role(role)
                .build();

        employeeRepo.save(emp);
        return emp;
    }

    public Response login(LoginRequest loginRequest){
        Optional<Employee> employee = employeeRepo.findByEmail(loginRequest.getEmail());
        if(employee.isEmpty()){
            throw new RuntimeException("User not found");
        }
        Employee emp = employee.get();
        Authentication authenticate = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            String token = jwtService.generateToken(emp);
            return new Response(token);
        }
        return new Response("Invalid credentials");
    }

    public List<Employee> getAllEmployees(){
        return employeeRepo.findAll();
    }
}
