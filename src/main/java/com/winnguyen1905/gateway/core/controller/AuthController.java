package com.winnguyen1905.gateway.core.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winnguyen1905.gateway.common.SystemConstant;
import com.winnguyen1905.gateway.core.model.request.LoginRequest;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;
import com.winnguyen1905.gateway.core.service.IAuthService;
import com.winnguyen1905.gateway.exception.ResourceNotFoundException;
import com.winnguyen1905.gateway.util.CookieUtils;
import com.winnguyen1905.gateway.util.MetaMessage;
import com.winnguyen1905.gateway.util.SecurityHolderUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

  private final CookieUtils cookieUtils;
  private final IAuthService authService;
  private final ReactiveJwtDecoder jwtDecoder;
  // private final IUserService userService;

  @PostMapping("/login")
  @MetaMessage(message = "Login success")
  public Mono<ResponseEntity<AuthResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
    return this.authService.handleLogin(loginRequest)
        .subscribeOn(Schedulers.boundedElastic())
        .map(authenResponse -> ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE, this.cookieUtils
                    .createCookie(SystemConstant.REFRESH_TOKEN, authenResponse.getTokens().getRefreshToken())
                    .toString())
            .body(authenResponse));
  }

  @PostMapping("/register")
  @MetaMessage(message = "Register success")
  public Mono<ResponseEntity<AuthResponse>> register(@RequestBody @Valid RegisterRequest registerRequest) {
    return this.authService.handleRegister(registerRequest)
        .subscribeOn(Schedulers.boundedElastic())
        .map(auth -> ResponseEntity.status(HttpStatus.CREATED).body(auth));
  }

  @PostMapping("/refresh")
  @MetaMessage(message = "Get user by refresh token success")
  public Mono<ResponseEntity<AuthResponse>> getAuthenticationResultByRefreshToken(
      @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
    if (refreshToken.isEmpty()) {
      throw new ResourceNotFoundException("Not found refresh token");
    }
    return this.jwtDecoder.decode(refreshToken)
        .flatMap(jwt -> this.authService.handleRefreshToken(jwt.getSubject(), refreshToken))
        .map(auth -> ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE,
                this.cookieUtils.createCookie(SystemConstant.REFRESH_TOKEN, auth.getTokens().getRefreshToken())
                    .toString())
            .body(auth));
  }

  // @GetMapping("/account")
  // @MetaMessage(message = "Get my account success")
  // public ResponseEntity<AuthResponse> getAccount() {
  // String username = SecurityHolderUtils.getCurrentUserLogin()
  // .orElseThrow(() -> new UsernameNotFoundException("Not found username"));
  // AuthResponse authenResponse =
  // AuthResponse.builder().user(this.userService.handleGetUserByUsername(username))
  // .build();
  // return ResponseEntity.ok().body(authenResponse);
  // }

  @PostMapping("/logout")
  public Mono<ResponseEntity<Void>> logout() {
    return Mono.just(SecurityHolderUtils.getCurrentUserLogin())
        .flatMap(optional -> Mono.justOrEmpty(optional))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found username")))
        .flatMap(username -> this.authService.handleLogout(username))
        .map(result -> ResponseEntity.status(HttpStatus.NO_CONTENT)
            .header(HttpHeaders.SET_COOKIE, this.cookieUtils.deleteCookie(SystemConstant.REFRESH_TOKEN).toString())
            .body(result));
  }
}
