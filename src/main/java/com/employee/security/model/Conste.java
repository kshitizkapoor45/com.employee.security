package com.employee.security.model;

public enum Conste {

    ADMIN(101,"Admin"),
    USER(102,"User"),
    MANAGER(103,"Manager");

    private final Integer code;
    private final String description;

    Conste(int code,String description){
        this.code = code;
        this.description = description;
    }
    public Integer getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
    public static Conste valueFromCode(int code){
        for(Conste status : values()){
            if(status.getCode() == code){
                return status;
            }
        }
        throw new RuntimeException("Invalid Code");
    }
}
