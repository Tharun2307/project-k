package com.mits.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mits.dto.LoginRequest;
import com.mits.dto.LoginResponse;
import com.mits.dto.SportAdminRequestLogin;
import com.mits.entity.User;
import com.mits.enums.Role;
import com.mits.repository.UserRepository;
import com.mits.security.JwtService;
import com.mits.service.AuthService;

@Service
public class AuthServiceImpl
        implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(
            LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        String token =
                jwtService.generateToken(
                        request.getEmail());

        return new LoginResponse(token);
    }

    @Override
    public String createSportAdmin(
            SportAdminRequestLogin request) {

        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));
        user.setRole(Role.SPORT_ADMIN);

        userRepository.save(user);

        return "Sport Admin Created Successfully";
    }
}
