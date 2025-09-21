package com.kholid.hrms.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kholid.hrms.model.LeaveApplication;
import com.kholid.hrms.model.User;
import com.kholid.hrms.service.LeaveService;
import com.kholid.hrms.service.UserService;

@Controller
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String listLeaves(Model model, Authentication auth) {
        String role = auth.getAuthorities()
                      .stream()
                      .findFirst()
                      .map(granted -> granted.getAuthority())
                      .orElse("ROLE_UNKNOWN");
        
    User currentUser = userService.getCurrentUser();

    List<LeaveApplication> leaves;

    if ("ROLE_EMPLOYEE".equals(role)) {
        // employee sees only their own leaves
        leaves = leaveService.getLeavesByUser(currentUser);
    } else if ("ROLE_MANAGER".equals(role)) {
        // manager sees leaves from their department
        leaves = leaveService.getLeavesByDepartment(currentUser.getDepartment());
    } else {
        // fallback → empty list
        leaves = List.of();
    }
                      
        model.addAttribute("role", role);
        model.addAttribute("leaves", leaves);
        return "leaves/list";
    }

    // Show add form
    @GetMapping("/form")
    public String showLeaveForm() {
        return "leaves/formLeave";
    }

    @PostMapping("/apply")
    public String applyLeave(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam("leaveReason") String leaveReason,
                             Model model, Authentication auth) {
        // Get logged-in user
        User user = userService.getCurrentUser();

        // Create leave
        String message = leaveService.createLeave(user, date, leaveReason);
        return buildLeavePage(model, auth, message);
    }

    // for approve application
    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id, Model model, Authentication auth) {
        String message = leaveService.approveLeave(id);
        return buildLeavePage(model, auth, message);
    }

    // for reject application
    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id, Model model, @RequestParam(required = true) String statusReason, Authentication auth) {
        String message = leaveService.rejectLeave(id, statusReason);
        return buildLeavePage(model, auth, message);
    }

    // for appeal reject
    @PostMapping("/appeal/{id}")
    public String appeal(@PathVariable Long id, Model model, @RequestParam(required = true) String appealReason, Authentication auth) {
        String message = leaveService.appealReject(id, appealReason);
        return buildLeavePage(model, auth, message);
    }

    private String buildLeavePage(Model model, Authentication auth, String message) {
        String role = auth.getAuthorities()
                          .stream()
                          .findFirst()
                          .map(granted -> granted.getAuthority())
                          .orElse("ROLE_UNKNOWN");
        
    User currentUser = userService.getCurrentUser();

    List<LeaveApplication> leaves;

    if ("ROLE_EMPLOYEE".equals(role)) {
        // employee sees only their own leaves
        leaves = leaveService.getLeavesByUser(currentUser);
    } else if ("ROLE_MANAGER".equals(role)) {
        // manager sees leaves from their department
        leaves = leaveService.getLeavesByDepartment(currentUser.getDepartment());
    } else {
        // fallback → empty list
        leaves = List.of();
    }
        model.addAttribute("message", message);
        model.addAttribute("role", role);
        model.addAttribute("leaves", leaves);
        return "leaves/list";
    }

}
