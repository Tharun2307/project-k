package com.mits.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mits.entity.User;
import com.mits.enums.Role;
import com.mits.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if an Event Coordinator already exists
        if (userRepository.findByRole(Role.EVENT_COORDINATOR).isEmpty()) {
            
            User coordinator = new User();
            coordinator.setName("Super Admin");
            coordinator.setEmail("coordinator@gmail.com");
            coordinator.setPassword(passwordEncoder.encode("admin123"));
            coordinator.setRole(Role.EVENT_COORDINATOR);
            
            userRepository.save(coordinator);
            
            System.out.println("=========================================");
            System.out.println("✅ SUCCESS: Initial Event Coordinator created!");
            System.out.println("👉 Email: coordinator@gmail.com");
            System.out.println("👉 Password: admin123");
            System.out.println("=========================================");
        }
    }
}