package com.kholid.hrms.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String listLeaves(Model model) {
        model.addAttribute("leaves", leaveService.getAllLeaves());
        return "leaves/list";
    }

    // Show add form
    @GetMapping("/form")
    public String showLeaveForm() {
        return "leaves/formLeave";
    }

    @PostMapping("/apply")
    public String applyLeave(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             @RequestParam("leaveReason") String leaveReason) {
        // Get logged-in user
        User user = userService.getCurrentUser();

        // Create leave
        leaveService.createLeave(user, date, leaveReason);

        return "redirect:/leaves"; // back to leaves after apply
    }
}
