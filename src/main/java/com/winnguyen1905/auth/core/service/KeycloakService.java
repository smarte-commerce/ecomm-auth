package com.winnguyen1905.auth.core.service;

import com.winnguyen1905.auth.core.model.request.LoginRequest;
import com.winnguyen1905.auth.core.model.request.RegisterRequest;
import com.winnguyen1905.auth.core.model.response.AuthResponse;
import com.winnguyen1905.auth.util.TokenPair;
import reactor.core.publisher.Mono;

public interface KeycloakService {
    
    /**
     * Authenticate user with Keycloak using username/password
     * @param loginRequest containing username and password
     * @return TokenPair with access and refresh tokens from Keycloak
     */
    Mono<TokenPair> authenticateWithKeycloak(LoginRequest loginRequest);
    
    /**
     * Register a new user in Keycloak
     * @param registerRequest containing user details
     * @return success indication
     */
    Mono<Void> registerUserInKeycloak(RegisterRequest registerRequest);
    
    /**
     * Refresh access token using refresh token
     * @param refreshToken the refresh token
     * @return new TokenPair
     */
    Mono<TokenPair> refreshToken(String refreshToken);
    
    /**
     * Logout user from Keycloak
     * @param refreshToken the refresh token to invalidate
     * @return success indication
     */
    Mono<Void> logoutFromKeycloak(String refreshToken);
    
    /**
     * Get authorization URL for OAuth2 flow
     * @param redirectUri the redirect URI after authentication
     * @return authorization URL
     */
    String getAuthorizationUrl(String redirectUri);
    
    /**
     * Exchange authorization code for tokens
     * @param code authorization code
     * @param redirectUri redirect URI used in authorization
     * @return TokenPair with access and refresh tokens
     */
    Mono<TokenPair> exchangeCodeForTokens(String code, String redirectUri);
} 
