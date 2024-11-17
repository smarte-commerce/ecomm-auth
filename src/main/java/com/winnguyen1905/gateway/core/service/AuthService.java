package com.winnguyen1905.gateway.core.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.core.converter.AuthenResponseConverter;
import com.winnguyen1905.gateway.core.converter.UserConverter;
import com.winnguyen1905.gateway.core.model.CustomUserDetails;
import com.winnguyen1905.gateway.core.model.request.LoginRequest;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;
import com.winnguyen1905.gateway.exception.ResourceAlreadyExistsException;
import com.winnguyen1905.gateway.exception.ResourceNotFoundException;
import com.winnguyen1905.gateway.persistance.entity.EUserCredentials;
import com.winnguyen1905.gateway.persistance.repository.RoleRepository;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;
import com.winnguyen1905.gateway.util.JwtUtils;
import com.winnguyen1905.gateway.util.TokenPair;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

  private final JwtUtils jwtUtils;
  private final ModelMapper mapper;
  private final UserConverter userConverter;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenResponseConverter authenResponseConverter;
  private final ReactiveAuthenticationManager reactiveAuthenticationManager;

  @Override
  public Mono<AuthResponse> handleLogin(LoginRequest loginRequest) {
    return this.reactiveAuthenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()))
        .map(authentication -> {
          if (!(authentication.getPrincipal() instanceof CustomUserDetails customUserDetails)) {
            throw new ClassCastException(
                "Unexpected principal type: " + authentication.getPrincipal().getClass());
          }
          return customUserDetails;
        })
        .map(userDetails -> {
          TokenPair tokenPair = jwtUtils.createTokenPair(userDetails);
          EUserCredentials userCredentials = mapper.map(userDetails, EUserCredentials.class);

          handleUpdateUsersRefreshToken(loginRequest.getUsername(), tokenPair.getRefreshToken())
              .subscribeOn(Schedulers.boundedElastic()).subscribe();
          return this.authenResponseConverter.toAuthenResponse(userCredentials, tokenPair);
        });
  }

  @Override
  public Mono<AuthResponse> handleRegister(RegisterRequest registerRequest) {
    EUserCredentials eUserCredentials = this.userRepository.findUserByUsername(registerRequest.getUsername())
        .orElse(null);
    if (eUserCredentials != null)
      throw new ResourceAlreadyExistsException("Username already exists");
    return Mono.just(userConverter.toUserEntity(registerRequest))
        .flatMap(user -> {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          return Mono.fromCallable(() -> roleRepository.findByCode("admin"))
              .flatMap(optionalRole -> Mono.justOrEmpty(optionalRole))
              .switchIfEmpty(Mono.error(new ResourceNotFoundException("Admin role not found")))
              .map(role -> {
                user.setRole(role);
                return this.userRepository.save(user);
              })
              .map(savedUser -> this.authenResponseConverter.toAuthenResponse(savedUser, null));
        });
  }

  private Mono<Void> handleUpdateUsersRefreshToken(String username, String refreshToken) {
    return Mono.fromCallable(() -> this.userRepository.findUserByUsername(username))
        .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found user by username " + username)))
        .doOnNext(user -> user.setRefreshToken(refreshToken))
        .flatMap(user -> Mono.fromCallable(() -> this.userRepository.save(user)))
        .then();
  }

  @Override
  public Mono<AuthResponse> handleRefreshToken(String username, String refreshToken) {
    return Mono.fromCallable(() -> this.userRepository.findByUsernameAndRefreshToken(username, refreshToken))
        .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found user by username " + username)))
        .map(user -> {
          TokenPair tokenPair = this.jwtUtils.createTokenPair(this.mapper.map(user, CustomUserDetails.class));
          handleUpdateUsersRefreshToken(username, tokenPair.getRefreshToken())
              .subscribeOn(Schedulers.boundedElastic())
              .subscribe();
          return this.authenResponseConverter.toAuthenResponse(user, tokenPair);
        });
  }

  @Override
  public Mono<Void> handleLogout(String username) {
    return Mono.fromCallable(() -> this.userRepository.findUserByUsername(username))
        .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found user by username " + username)))
        .doOnNext(user -> user.setRefreshToken(null))
        .flatMap(user -> Mono.fromCallable(() -> this.userRepository.save(user)).then());
  }

}
