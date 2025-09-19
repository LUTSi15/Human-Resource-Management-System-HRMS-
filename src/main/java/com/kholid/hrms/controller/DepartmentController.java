package com.kholid.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kholid.hrms.model.Department;
import com.kholid.hrms.model.User;
import com.kholid.hrms.repo.DepartmentRepository;
import com.kholid.hrms.repo.UserRepository;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    // List all departments
    @GetMapping("")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "departments/list";
    }

    // Show form to add new department
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("users", userRepository.findAll());
        return "departments/add";
    }

    // Handle saving new department
    @PostMapping("/add")
    public String saveDepartment(
            @ModelAttribute Department department,
            @RequestParam(required = false) Long managerId,
            Model model) {
            
        if (managerId != null) {
            // fetch existing manager from DB
            User manager = userRepository.findById(managerId).orElse(null);
            department.setManager(manager); // assign manager (may be null if not found)
        
            if (manager != null) {
                // Check if any other department already has this manager
                Department existingDept = departmentRepository.findByManager(manager);
                if (existingDept != null) {
                    // Manager already assigned to another department
                    model.addAttribute("errorMessage", "This manager is already assigned to another department!");
                
                    // reload users list for the dropdown
                    model.addAttribute("users", userRepository.findAll());
                
                    // reload department for form
                    model.addAttribute("department", department);
                
                    return "departments/add"; // return back to add page
                }
            }
        } else {
            department.setManager(null); // explicitly null
        }
    
        // save department if no errors
        departmentRepository.save(department);
        return "redirect:/departments";
    }

    // Show form to edit department
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("department", department);
        return "departments/edit";
    }

    // Handle updating department
    @PostMapping("/edit")
    public String updateDepartment(
            @ModelAttribute Department department,
            @RequestParam(required = false) Long managerId,
            Model model) {
            
        if (managerId != null) {
            // fetch existing manager from DB
            User manager = userRepository.findById(managerId).orElse(null);

            if (manager != null) {
                // Check if any other department already has this manager
                Department existingDept = departmentRepository.findByManager(manager);
                Department prevDepartment = departmentRepository.findById(department.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + department.getId()));;
                if (existingDept != null && existingDept != prevDepartment) {
                    // Manager already assigned to another department
                    model.addAttribute("errorMessage", "This manager is already assigned to another department!");

                    // reload users list for the dropdown
                    model.addAttribute("users", userRepository.findAll());

                    // reload department for form
                    model.addAttribute("department", prevDepartment);

                    return "departments/edit"; // return back to edit page
                }
            }
            department.setManager(manager); // may still be null if id not found
        } else {
            department.setManager(null); // explicitly set manager null
        }
    
        departmentRepository.save(department);
        return "redirect:/departments";
    }

    // Delete department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentRepository.deleteById(id);
        return "redirect:/departments";
    }
}
