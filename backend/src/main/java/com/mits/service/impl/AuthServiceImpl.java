package com.mits.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mits.dto.LoginRequest;
import com.mits.dto.LoginResponse;
import com.mits.dto.SportAdminRequestLogin;
import com.mits.entity.Sport;
import com.mits.entity.User;
import com.mits.enums.Role;
import com.mits.repository.SportRepository;
import com.mits.repository.UserRepository;
import com.mits.security.JwtService;
import com.mits.service.AuthService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SportRepository sportRepository; // ✅ NEW: Added to fetch sports

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(request.getEmail());

        return new LoginResponse(
            token,
            user.getEmail(),
            user.getRole().name(),
            user.getName()
        );
    }

    @Override
    public String createSportAdmin(SportAdminRequestLogin request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.SPORT_ADMIN);

        // ✅ NEW: Assign sports to the Sport Admin based on the IDs passed
        if (request.getSportIds() != null && !request.getSportIds().isEmpty()) {
            Set<Sport> sports = new HashSet<>(sportRepository.findAllById(request.getSportIds()));
            user.setAssignedSports(sports);
        }

        userRepository.save(user);
        return "Sport Admin Created Successfully";
    }

    @Override
    public List<User> getAllSportAdmins() {
        return userRepository.findByRole(Role.SPORT_ADMIN);
    }
    
    // ❌ REMOVED: createEventCoordinator() because we use DataInitializer instead for security.
}