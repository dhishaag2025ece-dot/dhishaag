package com.example.roombooking.service;

import com.example.roombooking.dto.LoginRequest;
import com.example.roombooking.dto.RegisterRequest;
import com.example.roombooking.entity.Employee;
import com.example.roombooking.exception.ApiException;
import com.example.roombooking.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public Employee register(RegisterRequest r) {
        if (repo.findByEmail(r.email()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }
        Employee e = new Employee();
        e.setName(r.name());
        e.setEmail(r.email());
        e.setPassword(r.password());
        e.setRole("USER");   // sign-up can never create an admin
        return repo.save(e);
    }

    public Employee login(LoginRequest r) {
        Employee e = repo.findByEmail(r.email())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!e.getPassword().equals(r.password()) || !e.getRole().equals(r.role())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password for this login type");
        }
        return e;
    }

    public List<Employee> listUsers(Long adminId) {
        requireAdmin(adminId);
        return repo.findByRole("USER");
    }

    public void requireAdmin(Long id) {
        if (id == null) throw new ApiException(HttpStatus.FORBIDDEN, "Admin access only");
        Employee e = repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "Admin access only"));
        if (!"ADMIN".equals(e.getRole())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Admin access only");
        }
    }

    public void createAdminIfMissing(String email, String password) {
        if (repo.findByEmail(email).isEmpty()) {
            Employee admin = new Employee();
            admin.setName("Administrator");
            admin.setEmail(email);
            admin.setPassword(password);
            admin.setRole("ADMIN");
            repo.save(admin);
        }
    }
}
