package com.kholid.hrms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.model.LeaveApplication;
import com.kholid.hrms.model.User;
import com.kholid.hrms.repo.LeaveRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public String createLeave(User user, LocalDate date, String leaveReason) {
        LeaveApplication leave = new LeaveApplication();
        leave.setUser(user);
        leave.setDate(date);
        leave.setLeaveReason(leaveReason);
        leave.setStatus("Apply");
        leave.setStatusReason(null);
        leave.setAppealReason(null);

        leaveRepository.save(leave);

        return "Leave application successfully sent";
    }

    public List<LeaveApplication> getLeavesByUser(User user) {
        return leaveRepository.findByUser(user);
    }

    public List<LeaveApplication> getLeavesByDepartment(Department department) {
        return leaveRepository.findByUserDepartmentId(department.getId());
    }

    public List<LeaveApplication> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public String approveLeave(Long id) {
        LeaveApplication leave = leaveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid leave Id:" + id));
        leave.setStatus("Approved");
        leaveRepository.save(leave);
        return "Successfully approved";
    }

    public String rejectLeave(Long id, String statusreason) {
        LeaveApplication leave = leaveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid leave Id:" + id));
        leave.setStatus("Reject");
        leave.setStatusReason(statusreason);
        leaveRepository.save(leave);
        return "Successfully reject";
    }

    public String appealReject(Long id, String appealReason) {
        LeaveApplication leave = leaveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid leave Id:" + id));
        leave.setStatus("Appeal");
        leave.setAppealReason(appealReason);
        leaveRepository.save(leave);
        return "Appeal Successfully sent";
    }
}

