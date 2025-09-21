package com.kholid.hrms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.service.DepartmentService;
import com.kholid.hrms.service.UserService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final UserService userService;

    public DepartmentController(DepartmentService departmentService, UserService userService) {
        this.departmentService = departmentService;
        this.userService = userService;
    }

    // List all departments
    @GetMapping("")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments/list";
    }

    // Show add form
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("users", userService.getAllUsers());
        return "departments/add";
    }

    // Handle saving new department
    @PostMapping("/add")
    public String saveDepartment(@ModelAttribute Department department,
                                 @RequestParam(required = false) Long managerId,
                                 Model model) {

        String error = departmentService.saveDepartment(department, managerId);
        if (error != null) {
            model.addAttribute("errorMessage", error);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("department", department);
            return "departments/add";
        }

        return "redirect:/departments";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartment(id);
        model.addAttribute("department", department);
        model.addAttribute("users", userService.getAllUsers());
        return "departments/edit";
    }

    // Handle updating department
    @PostMapping("/edit")
    public String updateDepartment(@ModelAttribute Department department,
                                   @RequestParam(required = false) Long managerId,
                                   Model model) {

        String error = departmentService.updateDepartment(department, managerId);
        if (error != null) {
            model.addAttribute("errorMessage", error);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("department", department);
            return "departments/edit";
        }

        return "redirect:/departments";
    }

    // Delete department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}