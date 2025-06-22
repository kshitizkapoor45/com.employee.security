package com.employee.security.util;

import com.employee.security.model.Conste;

public enum Permissions {

    CREATE_EMPLOYEE(201, "Create Employee"),
    EDIT_EMPLOYEE(202, "Edit Employee"),
    VIEW_EMPLOYEE(203, "View Employee"),
    DELETE_EMPLOYEE(204, "Delete Employee"),
    CREATE_DEPARTMENT(205, "Create Department"),
    EDIT_DEPARTMENT(206, "Edit Department"),
    VIEW_DEPARTMENT(207, "View Department"),
    DELETE_DEPARTMENT(208, "Delete Department");

    private final int code;
    private final String description;

    Permissions(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Permissions valueFromCode(int code){
        for(Permissions status : values()){
            if(status.getCode() == code){
                return status;
            }
        }
        throw new RuntimeException("Invalid Code");
    }
}