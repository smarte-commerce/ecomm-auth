package com.winnguyen1905.gateway.core.service;

import com.winnguyen1905.gateway.core.model.request.LoginRequest;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;

import reactor.core.publisher.Mono;

public interface IAuthService {
    public Mono<AuthResponse> handleLogin(LoginRequest loginRequest);

    public AuthResponse handleRegister(RegisterRequest registerRequest);

    public AuthResponse handleGetAuthenResponseByUsernameAndRefreshToken(String username, String refreshToken);

    public Void handleUpdateUsersRefreshToken(String username, String refreshToken);

    public Void handleLogout(String username);
}