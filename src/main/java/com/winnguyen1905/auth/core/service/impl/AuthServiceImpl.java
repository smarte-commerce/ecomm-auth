package com.winnguyen1905.auth.core.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.winnguyen1905.auth.common.constant.AccountType;
import com.winnguyen1905.auth.core.model.request.LoginRequest;
import com.winnguyen1905.auth.core.model.request.RegisterRequest;
import com.winnguyen1905.auth.core.model.response.AccountVm;
import com.winnguyen1905.auth.core.model.response.AuthResponse;
import com.winnguyen1905.auth.core.model.response.CustomUserDetails;
import com.winnguyen1905.auth.core.service.AuthService;
import com.winnguyen1905.auth.core.service.KeycloakService;
import com.winnguyen1905.auth.exception.ResourceAlreadyExistsException;
import com.winnguyen1905.auth.persistance.entity.EAccountCredentials;
import com.winnguyen1905.auth.persistance.entity.ECustomer;
import com.winnguyen1905.auth.persistance.entity.EVendor;
import com.winnguyen1905.auth.persistance.repository.UserRepository;
import com.winnguyen1905.auth.util.JwtUtils;
import com.winnguyen1905.auth.util.TokenPair;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtUtils jwtUtils;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ReactiveAuthenticationManager authenticationManager;
  private final KeycloakService keycloakService;

  @Value("${keycloak.direct-access-grants-enabled}")
  private boolean directAccessGrantsEnabled;

  @Override

  // TODO: no need to create new user here, just authenticate with Keycloak
  public Mono<AuthResponse> login(LoginRequest loginRequest, ServerWebExchange exchange) {
    log.info("Login attempt for user: {}", loginRequest.username());
    if (directAccessGrantsEnabled) {
      // Use Keycloak for authentication
      return keycloakService.authenticateWithKeycloakFullResponse(loginRequest)
          .flatMap(authResponse -> {
            // Find or create local user record for consistency
            return Mono.fromCallable(() -> userRepository.findUserByUsername(loginRequest.username()))
                .flatMap(optionalUser -> {
                  if (optionalUser.isPresent()) {
                    // Update existing user with new refresh token
                    EAccountCredentials user = optionalUser.get();
                    user.setRefreshToken(authResponse.refreshToken());
                    return Mono.fromCallable(() -> userRepository.save(user));
                  } else {
                    // User doesn't exist locally, this might happen if user was created directly in
                    // Keycloak
                    log.warn("User {} authenticated with Keycloak but not found in local database",
                        loginRequest.username());
                    // For now, we'll create a minimal user record
                    EAccountCredentials newUser = EAccountCredentials.builder()
                        .username(loginRequest.username())
                        .accountType(AccountType.CUSTOMER) // Default type
                        .status(true)
                        .refreshToken(authResponse.refreshToken())
                        .build();
                    return Mono.fromCallable(() -> userRepository.save(newUser));
                  }
                })
                .map(savedCredentials -> AuthResponse.builder()
                    .accessToken(authResponse.accessToken())
                    .refreshToken(authResponse.refreshToken())
                    .idToken(authResponse.idToken())
                    .expiresIn(authResponse.expiresIn())
                    .refreshExpiresIn(authResponse.refreshExpiresIn())
                    .tokenType(authResponse.tokenType())
                    .scope(authResponse.scope())
                    .account(AccountVm.builder()
                        .id(savedCredentials.getId())
                        .accountType(savedCredentials.getAccountType())
                        .username(savedCredentials.getUsername())
                        .status(savedCredentials.getStatus())
                        .build())
                    .build());
          })
          .doOnSuccess(
              authResponse -> log.info("Successfully authenticated user {} with Keycloak", loginRequest.username()))
          .doOnError(
              error -> log.error("Failed to authenticate user {} with Keycloak", loginRequest.username(), error));
    } else {
      // Fallback to local authentication (keep existing logic for backward
      // compatibility)
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()))
          .map(authentication -> (CustomUserDetails) authentication.getPrincipal())
          .flatMap(userDetails -> {
            try {
              TokenPair tokenPair = jwtUtils.createTokenPair(userDetails, exchange);
              return Mono.fromCallable(() -> userRepository.findById(userDetails.id()))
                  .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
                  .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                  .flatMap(userCredentials -> {
                    userCredentials.setRefreshToken(tokenPair.refreshToken());
                    return Mono.fromCallable(() -> userRepository.save(userCredentials))
                        .map(savedCredentials -> AuthResponse.builder()
                            .accessToken(tokenPair.accessToken())
                            .refreshToken(tokenPair.refreshToken())
                            .idToken(null) // Local JWT doesn't have ID token
                            .expiresIn(3600) // Default 1 hour
                            .refreshExpiresIn(1800) // Default 30 minutes
                            .tokenType("Bearer")
                            .scope("openid profile email")
                            .account(AccountVm.builder()
                                .id(savedCredentials.getId())
                                .accountType(savedCredentials.getAccountType())
                                .username(savedCredentials.getUsername())
                                .build())
                            .build());
                  });
            } catch (Exception e) {
              log.error("Failed to create token pair for user {}", loginRequest.username(), e);
              return Mono.error(new RuntimeException("Failed to create authentication tokens", e));
            }
          });
    }
  }

  @Override
  public Mono<Void> register(RegisterRequest registerRequest) {
    log.info("Registration attempt for user: {}", registerRequest.username());

    validateRegisterRequest(registerRequest);
    Optional<EAccountCredentials> user = this.userRepository.findUserByUsername(registerRequest.username());
    if (user.isPresent()) {
      throw new ResourceAlreadyExistsException("Username already exists");
    }

    var newAccount = EAccountCredentials.builder()
        .accountType(registerRequest.accountType())
        .username(registerRequest.username())
        .status(true)
        .customer(new ECustomer())
        .vendor(new EVendor())
        .password(passwordEncoder.encode(registerRequest.password())).build();
    newAccount.getCustomer().setAccountCredentials(newAccount);
    newAccount.getVendor().setAccountCredentials(newAccount);
    // if (registerRequest.accountType() == AccountType.CUSTOMER) {
    // ECustomer newCustomer = ECustomer.builder()
    // .customerName(registerRequest.fullName())
    // .customerAddress(registerRequest.address())
    // .customerEmail(registerRequest.email())
    // .customerPhone(registerRequest.phone())
    // .customerStatus(true)
    // .build();
    // newAccount.customer(newCustomer);
    // } else if (registerRequest.accountType() == AccountType.VENDOR) {
    // EVendor newVendor = EVendor.builder()
    // .vendorName(registerRequest.fullName())
    // .vendorAddress(registerRequest.address())
    // .vendorEmail(registerRequest.email())
    // .vendorPhone(registerRequest.phone())
    // .vendorStatus(true)
    // .build();
    // newAccount.vendor(newVendor);
    // }
    // return Mono.empty();

    if (directAccessGrantsEnabled) {
      return keycloakService.registerUserInKeycloak(registerRequest)
          .then(Mono.fromCallable(() -> this.userRepository.save(newAccount))).then()
          .doOnSuccess(v -> log.info("Successfully registered user {} in both Keycloak and local database",
              registerRequest.username()))
          .doOnError(error -> log.error("Failed to register user {} in Keycloak", registerRequest.username(), error));
    } else {
      // Only register locally if Keycloak is disabled
      return Mono.fromCallable(() -> this.userRepository.save(newAccount))
          .then()
          .doOnSuccess(v -> log.info("Successfully registered user {} in local database", registerRequest.username()));
    }
  }

  private void validateRegisterRequest(RegisterRequest request) {
    if (request == null || request.username() == null || request.password() == null) {
      throw new IllegalArgumentException("Username and password are required");
    }
    return;
  }

  // private AuthResponse mapToAuthResponse(EAccountCredentials user) {
  // return AuthResponse.builder()
  // .user(AccountVm.builder()
  // .username(user.getUsername())
  // .email(user.getEmail())
  // .build())
  // .build();
  // }

  // TODO:
  public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
      super(message);
    }
  }

  @Override
  public Mono<Void> logout(String username) {
    log.info("Logout attempt for user: {}", username);

    return Mono.fromCallable(() -> this.userRepository.findUserByUsername(username))
        .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found user by username " + username)))
        .flatMap(user -> {
          String refreshToken = user.getRefreshToken();
          user.setRefreshToken(null);
          Mono<Void> localLogout = Mono.fromCallable(() -> this.userRepository.save(user)).then();

          if (directAccessGrantsEnabled && refreshToken != null) {
            // Logout from both Keycloak and local
            return keycloakService.logoutFromKeycloak(refreshToken)
                .then(localLogout)
                .doOnSuccess(v -> log.info("Successfully logged out user {} from both Keycloak and local", username))
                .doOnError(error -> log.error("Failed to logout user {} from Keycloak, but local logout succeeded",
                    username, error));
          } else {
            // Only logout locally
            return localLogout
                .doOnSuccess(v -> log.info("Successfully logged out user {} locally", username));
          }
        });
  }

}
