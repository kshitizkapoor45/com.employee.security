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
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String,Object> login(LoginRequest loginRequest){
        Optional<Employee> employee = employeeRepo.findByEmail(loginRequest.getEmail());
        if(employee.isEmpty()){
            throw new RuntimeException("User not found");
        }
        Employee emp = employee.get();
        Authentication authenticate = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        HashMap<String,Object> response = new HashMap<>();
        if(authenticate.isAuthenticated()){
            String token = jwtService.generateToken(emp);
            String refreshToken = jwtService.generateRefreshToken(emp);
            response.put("access_token",token);
            response.put("refresh_token",refreshToken);

            return  response;
        }
        response.put("message","Invalid Credentials");
        return response;
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            return;
        }
        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){
            Employee employee = employeeRepo.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
            if(jwtService.isTokenValid(refreshToken,employee)){
                String accessToken = jwtService.generateToken(employee);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType("application/json");
                try {
                    response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
                } catch (IOException e) {
                    throw new RuntimeException("Error writing response", e);
                }
            } else {
                throw new RuntimeException("Invalid refresh token");
            }
        }
    }

    public List<Employee> getAllEmployees(){
        return employeeRepo.findAll();
    }
}
