package com.employee.security.util;

import lombok.Data;

@Data
public class ErrorResponse {
    private String userMessage;
    private String developerMessage;
    private String errorCode;
}
