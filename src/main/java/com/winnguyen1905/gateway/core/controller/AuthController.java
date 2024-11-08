package com.winnguyen1905.gateway.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.winnguyen1905.gateway.common.SystemConstant;
import com.winnguyen1905.gateway.core.model.request.LoginRequest;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;
import com.winnguyen1905.gateway.core.service.IAuthService;
import com.winnguyen1905.gateway.core.service.IUserService;
import com.winnguyen1905.gateway.util.CookieUtils;
import com.winnguyen1905.gateway.util.MetaMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("${release.api.prefix}/auth")
public class AuthController {

    private final IAuthService authService;
    private final IUserService userService;
    private final JwtDecoder jwtDecoder;
    private final CookieUtils cookieUtils;

    @PostMapping("/login")
    @MetaMessage(message = "Login success")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthResponse authenResponse = this.authService.handleLogin(loginRequest);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE,
                this.cookieUtils.createCookie(SystemConstant.REFRESH_TOKEN, authenResponse.getRefreshToken()).toString())
            .body(authenResponse);
    }

    @PostMapping("/register")
    @MetaMessage(message = "Register success")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        AuthResponse authenResponse = this.authService.handleRegister(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenResponse);
    }

    @PostMapping("/refresh")
    @MetaMessage(message = "Get user by refresh token success")
    public ResponseEntity<AuthResponse> getAuthenticationResultByRefreshToken(
        @CookieValue(name = "refresh_token", defaultValue = "Not found any refresh token") String refreshToken
    ) {
        Jwt jwt = this.jwtDecoder.decode(refreshToken);
        AuthResponse authenResponse = this.authService.handleGetAuthenResponseByUsernameAndRefreshToken(jwt.getSubject(), refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, this.cookieUtils.createCookie(SystemConstant.REFRESH_TOKEN, authenResponse.getRefreshToken()).toString())
                .body(authenResponse);
    }

    @GetMapping("/account")
    @MetaMessage(message = "Get my account success")
    public ResponseEntity<AuthResponse> getAccount() {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("Not found username"));
        AuthResponse authenResponse = AuthResponse.builder().userDTO(this.userService.handleGetUserByUsername(username)).build();
        return ResponseEntity.ok().body(authenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("Not found username"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.SET_COOKIE, this.cookieUtils.deleteCookie(SystemConstant.REFRESH_TOKEN).toString())
                .body(this.authService.handleLogout(username));
    }
    
}
