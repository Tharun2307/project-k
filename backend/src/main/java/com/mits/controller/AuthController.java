package com.mits.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.dto.LoginRequest;
import com.mits.dto.LoginResponse;
import com.mits.dto.SportAdminRequestLogin;
import com.mits.entity.User;
import com.mits.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ✅ PUBLIC: Login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // ✅ PROTECTED: Create Sport Admin (EVENT_COORDINATOR only)
    @PostMapping("/admin/create-sport-admin")
    public String createSportAdmin(@RequestBody SportAdminRequestLogin request) {
        return authService.createSportAdmin(request);
    }

    // ✅ PROTECTED: Get all Sport Admins (EVENT_COORDINATOR only)
    @GetMapping("/admin/sport-admins")
    public ResponseEntity<List<User>> getAllSportAdmins() {
        return ResponseEntity.ok(authService.getAllSportAdmins());
    }
}