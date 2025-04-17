package com.employee.security.util;

import com.employee.security.model.Conste;
import jakarta.persistence.AttributeConverter;

public class EnumConverter implements AttributeConverter<Conste,Integer> {
    @Override
    public Integer convertToDatabaseColumn(Conste conste) {
        if (conste == null) {
            return null;
        }
        return conste.getCode();
    }

    @Override
    public Conste convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return Conste.valueFromCode(code);
    }

}
