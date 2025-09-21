package com.kholid.hrms.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kholid.hrms.model.User;
import com.kholid.hrms.repo.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    }

    public User getUserbyUsername(String name) {
        return userRepository.findByUsername(name).orElse(null);
    }

    public void saveUser(User user) {
        // hash the password
        String encodedPassword = passwordEncoder().encode(user.getPassword());
        
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return null;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // returns username
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Invalid user username:" + username));
    }
}
