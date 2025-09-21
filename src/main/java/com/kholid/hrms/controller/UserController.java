package com.kholid.hrms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.model.User;
import com.kholid.hrms.service.DepartmentService;
import com.kholid.hrms.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("users")
public class UserController {
    
    private UserService userService;
    private DepartmentService departmentService;

    public UserController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    // List all users
    @GetMapping("")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", List.of("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER"));
        model.addAttribute("rolesName", List.of("Admin", "Employee", "Manager"));
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "users/add";
    }

    // Save new user
    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute User user,
                          BindingResult bindingResult,
                          @RequestParam(required = false) Long departmentId,
                          Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", List.of("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER"));
        model.addAttribute("rolesName", List.of("Admin", "Employee", "Manager"));
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "users/add";
        }

        if (departmentId != null) { // Department not null
            Department dept = departmentService.getDepartment(departmentId);
            user.setDepartment(dept);
        } else {
            user.setDepartment(null);
        }

        userService.saveUser(user);
        return "redirect:/users";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", List.of("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER"));
        model.addAttribute("rolesName", List.of("Admin", "Employee", "Manager"));
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "users/edit";
    }

    // Update user
    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute User user,
                             BindingResult bindingResult,
                             @RequestParam(required = false) Long departmentId,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", List.of("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER"));
            model.addAttribute("rolesName", List.of("Admin", "Employee", "Manager"));
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "users/edit";
        }

        if (departmentId != null) {
            Department dept = departmentService.getDepartment(departmentId);
            user.setDepartment(dept);
        } else {
            user.setDepartment(null);
        }

        userService.saveUser(user);
        return "redirect:/users";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        String error = userService.deleteUser(id);
        if (error != null){
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("errorMessage", error);
            return "users/list";
        }
        return "redirect:/users";
    }
}
