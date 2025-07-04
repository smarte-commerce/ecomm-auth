package com.winnguyen1905.auth.core.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.common.constant.SystemConstant;
import com.winnguyen1905.auth.core.model.request.LoginRequest;
import com.winnguyen1905.auth.core.model.request.RegisterRequest;
import com.winnguyen1905.auth.core.model.response.AuthResponse;
import com.winnguyen1905.auth.core.service.AccountServiceInterface;
import com.winnguyen1905.auth.core.service.AuthService;
import com.winnguyen1905.auth.util.CookieUtils;
import com.winnguyen1905.auth.util.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

  private final AuthService authService;
  private final AccountServiceInterface accountService;

  @PostMapping("/login")
  @ResponseMessage(message = "Login success")
  public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest loginRequest, ServerWebExchange exchange) {
    return this.authService.login(loginRequest, exchange)
        .subscribeOn(Schedulers.boundedElastic())
        .map(authenResponse -> ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE, CookieUtils
                    .createCookie(SystemConstant.REFRESH_TOKEN, authenResponse.tokens().refreshToken())
                    .toString())
            .body(authenResponse));
  }

  @PostMapping("/register")
  @ResponseMessage(message = "Register success")
  public Mono<ResponseEntity<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
    return this.authService.register(registerRequest)
        .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
  }

  @GetMapping("/account")
  @ResponseMessage(message = "Get my account success")
  public Mono<ResponseEntity<AuthResponse>> getAccount() {
    return Mono.justOrEmpty(SecurityUtils.getCurrentUserLogin())
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found username")))
        .flatMap(username -> this.accountService.getUserByUsername(username))
        .map(user -> ResponseEntity.ok(AuthResponse.builder().account(user).build()));
  }

  @PostMapping("/logout")
  public Mono<ResponseEntity<Void>> logout() {
    return Mono.justOrEmpty(SecurityUtils.getCurrentUserLogin())
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found username")))
        .flatMap(username -> this.authService.logout(username))
        .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT)
            .header(HttpHeaders.SET_COOKIE, CookieUtils.deleteCookie(SystemConstant.REFRESH_TOKEN).toString())
            .build());
  }
}
