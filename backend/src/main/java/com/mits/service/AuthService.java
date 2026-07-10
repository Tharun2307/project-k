package com.mits.service;

import com.mits.dto.LoginRequest;
import com.mits.dto.LoginResponse;
import com.mits.dto.SportAdminRequestLogin;
import com.mits.entity.User;

import java.util.List;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    String createSportAdmin(SportAdminRequestLogin request);
    
    
    // ✅ NEW: Get all Sport Admins
    List<User> getAllSportAdmins();
}