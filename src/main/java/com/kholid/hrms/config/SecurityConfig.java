package com.kholid.hrms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // where to load users
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());       // how to check passwords
        return provider;
    }
   
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers("/login", "/error").permitAll()

                // H2 console access
                .requestMatchers("/h2-console/**").permitAll()

                // Admin pages
                .requestMatchers("/departments/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/users/**").hasAuthority("ROLE_ADMIN")

                // Employee pages
                .requestMatchers("/leave/apply").hasAuthority("ROLE_EMPLOYEE")

                // Shared pages (Employee + Manager)
                .requestMatchers("/leave/list").hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_MANAGER")

                // Dashboard accessible by all logged-in users
                .requestMatchers("/dashboard").authenticated()

                // Any other request needs login
                .anyRequest().authenticated()
            )
            // .formLogin(form -> form
            //     .loginPage("/login")
            //     .defaultSuccessUrl("/dashboard", true)
            //     .failureUrl("/login?error=true")
            //     .permitAll()
            // )
            .formLogin(Customizer.withDefaults()) 
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) 
            .headers(headers -> headers.frameOptions().sameOrigin()); // allow frames
;

        return http.build();
    }
}