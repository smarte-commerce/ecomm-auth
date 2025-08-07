package com.winnguyen1905.auth.core.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.winnguyen1905.auth.common.annotation.ResponseMessage;
import com.winnguyen1905.auth.common.constant.SystemConstant;
import com.winnguyen1905.auth.core.model.request.LoginRequest;
import com.winnguyen1905.auth.core.model.request.RegisterRequest;
import com.winnguyen1905.auth.core.model.response.AuthResponse;
import com.winnguyen1905.auth.core.service.AccountServiceInterface;
import com.winnguyen1905.auth.core.service.AuthService;
import com.winnguyen1905.auth.core.service.KeycloakService;
import com.winnguyen1905.auth.util.CookieUtils;
import com.winnguyen1905.auth.util.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

  private final AuthService authService;
  private final AccountServiceInterface accountService;
  private final KeycloakService keycloakService;

  @Value("${keycloak.direct-access-grants-enabled:true}")
  private boolean directAccessGrantsEnabled;

  @PostMapping("/login")
  @ResponseMessage(message = "Login success")
  public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
    return this.authService.login(loginRequest, null)
        .subscribeOn(Schedulers.boundedElastic())
        .map(authenResponse -> ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE, CookieUtils
                    .createCookie(SystemConstant.REFRESH_TOKEN, authenResponse.refreshToken())
                    .toString())
            .body(authenResponse));
  }

  @PostMapping("/register")
  @ResponseMessage(message = "Register success")
  public Mono<ResponseEntity<Void>> register(@RequestBody RegisterRequest registerRequest) {
    return this.authService.register(registerRequest)
        .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
  }

  @GetMapping("/account")
  @ResponseMessage(message = "Get my account success")
  public Mono<ResponseEntity<AuthResponse>> getAccount() {
    return Mono.justOrEmpty(SecurityUtils.getCurrentUserLogin())
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found username")))
        .flatMap(username -> this.accountService.getUserByUsername(username))
        .map(user -> ResponseEntity.ok(
            AuthResponse.builder()
                .account(user)
                .build()));
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

  // OAuth2 / Keycloak endpoints

  /**
   * Get OAuth2 authorization URL for redirect to Keycloak
   */
  @GetMapping("/oauth2/authorize")
  @ResponseMessage(message = "Authorization URL generated")
  public Mono<ResponseEntity<String>> getAuthorizationUrl(
      @RequestParam(defaultValue = "http://localhost:8092/auth/oauth2/callback") String redirectUri) {

    if (!directAccessGrantsEnabled) {
      return Mono.just(ResponseEntity.badRequest().body("OAuth2 flow is disabled"));
    }

    try {
      String authUrl = keycloakService.getAuthorizationUrl(redirectUri);
      log.info("Generated authorization URL for redirect_uri: {}", redirectUri);
      return Mono.just(ResponseEntity.ok(authUrl));
    } catch (Exception e) {
      log.error("Failed to generate authorization URL", e);
      return Mono.just(ResponseEntity.internalServerError().body("Failed to generate authorization URL"));
    }
  }

  /**
   * Handle OAuth2 callback and exchange code for tokens
   */
  @GetMapping("/oauth2/callback")
  @ResponseMessage(message = "OAuth2 authentication success")
  public Mono<ResponseEntity<Object>> handleOAuth2Callback(
      @RequestParam String code,
      @RequestParam(defaultValue = "http://localhost:8092/auth/oauth2/callback") String redirectUri,
      ServerWebExchange exchange) {

    if (!directAccessGrantsEnabled) {
      return Mono.just(ResponseEntity.badRequest().body("OAuth2 flow is disabled"));
    }

    log.info("Handling OAuth2 callback with code: {}", code.substring(0, Math.min(code.length(), 10)) + "...");

    return keycloakService.exchangeCodeForTokensFullResponse(code, redirectUri)
        .map(authResponse -> {
          // Set cookies and redirect to frontend
          String frontendUrl = "http://localhost:3000/dashboard"; // Configure this hểr

          return ResponseEntity.status(HttpStatus.FOUND)
              .location(URI.create(frontendUrl))
              .header(HttpHeaders.SET_COOKIE,
                  CookieUtils.createCookie(SystemConstant.REFRESH_TOKEN, authResponse.refreshToken()).toString())
              .header(HttpHeaders.SET_COOKIE,
                  CookieUtils.createCookie("access_token", authResponse.accessToken()).toString())
              .build();
        })
        .doOnError(error -> log.error("Failed to exchange authorization code for tokens", error))
        .onErrorReturn(ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000/login?error=oauth_failed"))
            .build());
  }

  /**
   * Refresh access token using refresh token
   */
  @PostMapping("/refresh")
  @ResponseMessage(message = "Token refresh success")
  public Mono<ResponseEntity<AuthResponse>> refreshToken(
      @CookieValue(name = SystemConstant.REFRESH_TOKEN, required = false) String refreshToken) {

    if (!directAccessGrantsEnabled) {
      return Mono.just(ResponseEntity.badRequest().build());
    }

    if (refreshToken == null || refreshToken.isEmpty()) {
      log.warn("Refresh token is missing");
      return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    log.info("Refreshing access token");

    return keycloakService.refreshTokenFullResponse(refreshToken)
        .map(authResponse -> ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE,
                CookieUtils.createCookie(SystemConstant.REFRESH_TOKEN, authResponse.refreshToken()).toString())
            .body(authResponse))
        .doOnError(error -> log.error("Failed to refresh token", error))
        .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  /**
   * Check if user is authenticated (for frontend)
   */
  @GetMapping("/status")
  @ResponseMessage(message = "Authentication status")
  public Mono<ResponseEntity<AuthStatusResponse>> getAuthStatus() {
    return Mono.justOrEmpty(SecurityUtils.getCurrentUserLogin())
        .map(username -> ResponseEntity.ok().body(new AuthStatusResponse(true, username)))
        .defaultIfEmpty(ResponseEntity.ok().body(new AuthStatusResponse(false, null)));
  }

  // Helper record for auth status response
  private record AuthStatusResponse(boolean authenticated, String username) {
  }
}
