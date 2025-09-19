package com.kholid.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kholid.hrms.repo.UserRepository;

@Controller
@RequestMapping("users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
}
