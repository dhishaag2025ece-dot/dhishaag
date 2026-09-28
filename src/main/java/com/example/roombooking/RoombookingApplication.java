package com.example.roombooking;

import com.example.roombooking.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RoombookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoombookingApplication.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeService employeeService) {
		return args -> employeeService.createAdminIfMissing("admin@test.com", "admin123");
	}
}