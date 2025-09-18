package com.kholid.hrms.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kholid.hrms.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Find a user based on  username
}
