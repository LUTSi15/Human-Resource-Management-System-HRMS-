package com.kholid.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kholid.hrms.model.LeaveApplication;
import com.kholid.hrms.model.User;

public interface LeaveRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findByUser(User user); // Find list of leave based on user
    
    List<LeaveApplication> findByUserDepartmentId(Long id); // Find list of leaves based on department
}
