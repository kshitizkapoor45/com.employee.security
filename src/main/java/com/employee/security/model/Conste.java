package com.employee.security.model;

public enum Conste {

    ADMIN(101),
    USER(102);

    private final Integer code;

    Conste(int code){
        this.code = code;
    }
    public Integer getCode() {
        return code;
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
