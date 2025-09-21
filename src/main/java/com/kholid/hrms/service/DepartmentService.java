package com.kholid.hrms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.model.User;
import com.kholid.hrms.repo.DepartmentRepository;
import com.kholid.hrms.repo.UserRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    // List all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get department by ID
    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
    }

    // Save new department
    public String saveDepartment(Department department, Long managerId) {
        if (managerId != null) {
            User manager = userRepository.findById(managerId).orElse(null);
            department.setManager(manager);

            if (manager != null) {
                Department existingDept = departmentRepository.findByManager(manager);
                if (existingDept != null) {
                    return "Manager already assigned to another department!";
                }
            }
        } else {
            department.setManager(null);
        }

        departmentRepository.save(department);
        return null; // null means no error
    }

    // Update existing department
    public String updateDepartment(Department department, Long managerId) {
        if (managerId != null) {
            User manager = userRepository.findById(managerId).orElse(null);
            department.setManager(manager);

            if (manager != null) {
                Department existingDept = departmentRepository.findByManager(manager);
                Department prevDepartment = getDepartment(department.getId());
                if (existingDept != null && !existingDept.getId().equals(prevDepartment.getId())) {
                    return "Manager already assigned to another department!";
                }
            }
        } else {
            department.setManager(null);
        }

        departmentRepository.save(department);
        return null;
    }

    // Delete department
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
