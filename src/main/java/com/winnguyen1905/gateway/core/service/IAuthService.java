package com.winnguyen1905.gateway.core.service;

import com.winnguyen1905.gateway.core.model.request.LoginRequest;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;

import reactor.core.publisher.Mono;

public interface IAuthService {
  public Mono<Void> handleLogout(String username);

  public Mono<AuthResponse> handleLogin(LoginRequest loginRequest);

  public Mono<AuthResponse> handleRegister(RegisterRequest registerRequest);

  public Mono<AuthResponse> handleRefreshToken(String username, String refreshToken);
}
