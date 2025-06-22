package com.employee.security.controller;

import com.employee.security.model.Employee;
import com.employee.security.model.EmployeeRequest;
import com.employee.security.service.EmployeeService;
import com.employee.security.util.LoginRequest;
import com.employee.security.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        Response token = employeeService.login(loginRequest);
        return ResponseEntity.ok(token);
    }
}
