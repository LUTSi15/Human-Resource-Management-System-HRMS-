package com.kholid.hrms.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "leaves")
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee who applied for leave
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate date;

    @Column(name = "leave_reason")
    private String leaveReason;

    // PENDING / APPROVED / REJECTED
    private String status;

    // Reason when manager rejects
    private String statusReason;

    // Reason when employee appeals
    private String appealReason;

    // getters & setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public User getUser() { 
        return user; 
    }

    public void setUser(User user) { 
        this.user = user; 
    }

    public LocalDate getDate() { 
        return date; 
    }

    public void setDate(LocalDate date) { 
        this.date = date; 
    }

    public String getLeaveReason() { 
        return leaveReason; 
    }

    public void setLeaveReason(String leaveReason) { 
        this.leaveReason = leaveReason; 
    }

    public String getStatus() { 
        return status; 
    }

    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getStatusReason() { 
        return statusReason; 
    }

    public void setStatusReason(String statusReason) { 
        this.statusReason = statusReason; 
    }

    public String getAppealReason() { 
        return appealReason; 
    }

    public void setAppealReason(String appealReason) { 
        this.appealReason = appealReason; 
    }

}