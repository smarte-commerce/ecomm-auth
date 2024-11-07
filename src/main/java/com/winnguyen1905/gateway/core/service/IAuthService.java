package com.winnguyen1905.gateway.core.service;

import com.winnguyen1905.gateway.model.request.LoginRequest;
import com.winnguyen1905.gateway.model.request.RegisterRequest;
import com.winnguyen1905.gateway.model.response.AuthResponse;

public interface IAuthService {
    public AuthResponse handleLogin(LoginRequest loginRequest);

    public AuthResponse handleRegister(RegisterRequest registerRequest);

    public AuthResponse handleGetAuthenResponseByUsernameAndRefreshToken(String username, String refreshToken);

    public Void handleUpdateUsersRefreshToken(String username, String refreshToken);

    public Void handleLogout(String username);
}