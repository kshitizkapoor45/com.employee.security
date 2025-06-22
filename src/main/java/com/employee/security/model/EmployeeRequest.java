package com.employee.security.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
    private String name;
    private String email;
    private String password;
    private Conste role;
}
