package com.kholid.hrms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kholid.hrms.model.LeaveApplication;
import com.kholid.hrms.model.User;
import com.kholid.hrms.repo.LeaveRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public LeaveApplication createLeave(User user, LocalDate date, String leaveReason) {
        LeaveApplication leave = new LeaveApplication();
        leave.setUser(user);
        leave.setDate(date);
        leave.setLeaveReason(leaveReason);
        leave.setStatus("APPLY");
        leave.setStatusReason(null);
        leave.setAppealReason(null);

        return leaveRepository.save(leave);
    }

    public List<LeaveApplication> getLeavesByUser(User user) {
        return leaveRepository.findByUser(user);
    }

    public List<LeaveApplication> getAllLeaves() {
        return leaveRepository.findAll();
    }
}

