package com.kholid.hrms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.model.User;
import com.kholid.hrms.repo.DepartmentRepository;
import com.kholid.hrms.repo.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, DepartmentRepository departmentRepository) {
        return args -> {

            // Check if Admin user already exists
            if (userRepository.findByUsername("Admin").isEmpty()) {

                // 1. Create Department
                Department hrDept = new Department();
                hrDept.setName("Human Resource");
                // save first to generate ID
                departmentRepository.save(hrDept);

                // 2. Create Admin User
                User admin = new User();
                admin.setUsername("Admin");
                admin.setPassword("admin123"); // {noop} means plain text, for testing only
                admin.setRole("ROLE_ADMIN");
                admin.setDepartment(hrDept); // assign department
                userRepository.save(admin);

                // 3. Set manager of the department to Admin
                hrDept.setManager(admin);
                departmentRepository.save(hrDept);

                System.out.println("Default Admin and Human Resource department created.");
            } else {
                System.out.println("Admin user already exists. Skipping initialization.");
            }
        };
    }
}
