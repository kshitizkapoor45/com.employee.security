package com.employee.security.util;

import com.employee.security.model.Conste;
import jakarta.persistence.AttributeConverter;

public class PermissionConverter implements AttributeConverter<Permissions,Integer> {
    @Override
    public Integer convertToDatabaseColumn(Permissions attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public Permissions convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return Permissions.valueFromCode(code);
    }
}
