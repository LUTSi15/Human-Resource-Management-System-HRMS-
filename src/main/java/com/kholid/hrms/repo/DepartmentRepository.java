package com.kholid.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.model.User;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByManager(User manager);
}
