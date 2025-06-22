package com.employee.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('VIEW_DEPARTMENT')")
    public String getAdminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }
}
