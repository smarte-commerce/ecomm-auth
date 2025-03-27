package com.winnguyen1905.auth.core.service;

import java.util.Optional;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.winnguyen1905.auth.core.model.request.LoginRequest;
import com.winnguyen1905.auth.core.model.request.RegisterRequest;
import com.winnguyen1905.auth.core.model.response.AccountVm;
import com.winnguyen1905.auth.core.model.response.AuthResponse;
import com.winnguyen1905.auth.core.model.response.CustomUserDetails;
import com.winnguyen1905.auth.exception.ResourceAlreadyExistsException;
import com.winnguyen1905.auth.persistance.entity.EAccountCredentials;
import com.winnguyen1905.auth.persistance.repository.RoleRepository;
import com.winnguyen1905.auth.persistance.repository.UserRepository;
import com.winnguyen1905.auth.util.JwtUtils;
import com.winnguyen1905.auth.util.TokenPair;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtUtils jwtUtils;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ReactiveAuthenticationManager authenticationManager;

  @Override
  public Mono<AuthResponse> login(LoginRequest loginRequest) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()))
        .cast(CustomUserDetails.class)
        .flatMap(userDetails -> {
          TokenPair tokenPair = jwtUtils.createTokenPair(userDetails);
          return Mono.fromCallable(() -> userRepository.findById(userDetails.id()))
              .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
              .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
              .flatMap(userCredentials -> {
                userCredentials.setRefreshToken(tokenPair.refreshToken());
                return Mono.fromCallable(() -> userRepository.save(userCredentials))
                    .map(savedCredentials -> AuthResponse.builder()
                        .tokens(TokenPair.builder()
                            .accessToken(tokenPair.accessToken())
                            .refreshToken(tokenPair.refreshToken())
                            .build())
                        // .user(AccountVm.builder()
                        // .username(savedCredentials.getUsername())
                        // .email(savedCredentials.getEmail())
                        // .build())
                        .build());
              });
        });
  }

  @Override
  public Mono<Void> register(RegisterRequest registerRequest) {
    validateRegisterRequest(registerRequest);
    Optional<EAccountCredentials> user = this.userRepository.findUserByUsername(registerRequest.username());
    if (user.isPresent()) {
      throw new ResourceAlreadyExistsException("Username already exists");
    }
    EAccountCredentials newAccount = EAccountCredentials.builder()
        .username(registerRequest.username())
        .password(passwordEncoder.encode(registerRequest.password()))
        .build();
    return Mono.fromCallable(() -> this.userRepository.save(newAccount)).then();
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
    return Mono.fromCallable(() -> this.userRepository.findUserByUsername(username))
        .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found user by username " + username)))
        .doOnNext(user -> user.setRefreshToken(null))
        .flatMap(user -> Mono.fromCallable(() -> this.userRepository.save(user)).then());
  }

}
