package com.mits.service;

import com.mits.dto.SportAdminRequestLogin;
import com.mits.dto.LoginRequest;
import com.mits.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    String createSportAdmin(
            SportAdminRequestLogin request);
}