package com.kholid.hrms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.repo.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
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
    public String saveDepartment(Department department) {

        departmentRepository.save(department);
        return null; // null means no error
    }

    // Update existing department
    public String updateDepartment(Department department) {

        departmentRepository.save(department);
        return null;
    }

    // Delete department
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
