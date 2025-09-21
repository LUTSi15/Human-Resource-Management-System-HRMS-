package com.kholid.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kholid.hrms.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
