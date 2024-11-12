package com.winnguyen1905.gateway.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

  // private final JwtDecoder jwtDecoder;
  // private final CookieUtils cookieUtils;
  // private final IAuthService authService;
  // private final IUserService userService;

  // @PostMapping("/login")
  // @MetaMessage(message = "Login success")
  // public ResponseEntity<Mono<AuthResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
  //   Mono<AuthResponse> authResponse = this.authService.handleLogin(loginRequest);
  //   return ResponseEntity.ok()
  //       .header(HttpHeaders.SET_COOKIE,
  //           this.cookieUtils
  //               .createCookie(SystemConstant.REFRESH_TOKEN,
  //                   authResponse.map(e -> e.getTokens().getRefreshToken()).toString())
  //               .toString())
  //       .body(authResponse);
  // }

  // @PostMapping("/register")
  // @MetaMessage(message = "Register success")
  // public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
  //   AuthResponse authenResponse = this.authService.handleRegister(registerRequest);
  //   return ResponseEntity.status(HttpStatus.CREATED).body(authenResponse);
  // }

  // @PostMapping("/refresh")
  // @MetaMessage(message = "Get user by refresh token success")
  // public ResponseEntity<AuthResponse> getAuthenticationResultByRefreshToken(
  //     @CookieValue(name = "refresh_token", defaultValue = "Not found any refresh token") String refreshToken) {
  //   Jwt jwt = this.jwtDecoder.decode(refreshToken);
  //   AuthResponse authenResponse = this.authService.handleGetAuthenResponseByUsernameAndRefreshToken(jwt.getSubject(),
  //       refreshToken);
  //   return ResponseEntity.ok()
  //       .header(HttpHeaders.SET_COOKIE,
  //           this.cookieUtils.createCookie(SystemConstant.REFRESH_TOKEN, authenResponse.getTokens().getRefreshToken())
  //               .toString())
  //       .body(authenResponse);
  // }

  // @GetMapping("/account")
  // @MetaMessage(message = "Get my account success")
  // public ResponseEntity<AuthResponse> getAccount() {
  //   String username = SecurityHolderUtils.getCurrentUserLogin()
  //       .orElseThrow(() -> new UsernameNotFoundException("Not found username"));
  //   AuthResponse authenResponse = AuthResponse.builder().user(this.userService.handleGetUserByUsername(username))
  //       .build();
  //   return ResponseEntity.ok().body(authenResponse);
  // }

  // @PostMapping("/logout")
  // public ResponseEntity<Mono<Void>> logout() {
  //   String username = SecurityHolderUtils.getCurrentUserLogin()
  //       .orElseThrow(() -> new UsernameNotFoundException("Not found username"));
  //   return ResponseEntity.status(HttpStatus.NO_CONTENT)
  //       .header(HttpHeaders.SET_COOKIE, this.cookieUtils.deleteCookie(SystemConstant.REFRESH_TOKEN).toString())
  //       .body(this.authService.handleLogout(username));
  // }
}
