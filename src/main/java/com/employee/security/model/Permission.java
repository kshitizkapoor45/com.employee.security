package com.employee.security.model;

import com.employee.security.util.EnumConverter;
import com.employee.security.util.PermissionConverter;
import com.employee.security.util.Permissions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Permission {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Convert(converter = PermissionConverter.class)
    @Column(nullable = false)
    private Permissions name;

    private String description;
}