package com.employee.security.controller;

import com.employee.security.model.Employee;
import com.employee.security.model.EmployeeRequest;
import com.employee.security.service.EmployeeService;
import com.employee.security.util.LoginRequest;
import com.employee.security.util.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class AuthController {

    private final EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeRequest employee){
        Employee emp = employeeService.createEmployee(employee);
        return ResponseEntity.ok("Employee created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest loginRequest){
        Map<String,Object> response = employeeService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        employeeService.refreshToken(request,response);
    }
}
