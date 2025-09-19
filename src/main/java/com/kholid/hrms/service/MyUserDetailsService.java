package com.kholid.hrms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kholid.hrms.model.CustomUserDetails;
import com.kholid.hrms.model.User;
import com.kholid.hrms.repo.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // return org.springframework.security.core.userdetails.User
        //         .withUsername(user.getUsername())
        //         .password(user.getPassword()) // password should be encoded!
        //         .roles(user.getRole())
        //         .build();

        return new CustomUserDetails(user);
    }
}