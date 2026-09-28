package com.example.roombooking.controller;

import com.example.roombooking.dto.LoginRequest;
import com.example.roombooking.dto.RegisterRequest;
import com.example.roombooking.entity.Employee;
import com.example.roombooking.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Employee> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Employee> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(employeeService.login(request));
    }

    // Admin only: pass admin's id in header  X-Admin-Id
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> listUsers(
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        return ResponseEntity.ok(employeeService.listUsers(adminId));
    }
}