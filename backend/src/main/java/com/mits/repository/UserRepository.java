package com.mits.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mits.entity.User;
import com.mits.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    // ✅ NEW: Find all users by role
    List<User> findByRole(Role role);
    
    // ✅ NEW: Check if email exists
    boolean existsByEmail(String email);
}