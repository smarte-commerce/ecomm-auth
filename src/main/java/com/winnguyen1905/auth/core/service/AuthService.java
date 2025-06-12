package com.winnguyen1905.auth.core.service;

import org.springframework.web.server.ServerWebExchange;

import com.winnguyen1905.auth.core.model.request.LoginRequest;
import com.winnguyen1905.auth.core.model.request.RegisterRequest;
import com.winnguyen1905.auth.core.model.response.AuthResponse;

import reactor.core.publisher.Mono;

public interface AuthService {
  public Mono<Void> logout(String username);

  public Mono<AuthResponse> login(LoginRequest loginRequest, ServerWebExchange exchange);

  public Mono<Void> register(RegisterRequest registerRequest);
}
