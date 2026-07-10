package com.mits.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mits.dto.LoginRequest;
import com.mits.dto.LoginResponse;
import com.mits.dto.SportAdminRequestLogin;
import com.mits.service.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {
    	
        return authService.login(request);
    }

    @PostMapping("/admin/create-sport-admin")
    public String createSportAdmin(
            @RequestBody
            	SportAdminRequestLogin request) {

        return authService
                .createSportAdmin(request);
    }
}