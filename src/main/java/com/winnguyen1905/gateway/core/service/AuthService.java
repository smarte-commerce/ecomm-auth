package com.winnguyen1905.gateway.core.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.config.CustomReactiveAuthManager;
import com.winnguyen1905.gateway.core.converter.AuthenResponseConverter;
import com.winnguyen1905.gateway.core.converter.UserConverter;
import com.winnguyen1905.gateway.core.model.CustomUserDetails;
import com.winnguyen1905.gateway.core.model.User;
import com.winnguyen1905.gateway.core.model.request.LoginRequest;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;
import com.winnguyen1905.gateway.persistance.entity.ERole;
import com.winnguyen1905.gateway.persistance.entity.EUser;
import com.winnguyen1905.gateway.persistance.repository.RoleRepository;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;
import com.winnguyen1905.gateway.util.AuthenticationUtils;
import com.winnguyen1905.gateway.util.JwtUtils;
import com.winnguyen1905.gateway.util.TokenPair;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Transactional
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
    Mono<Authentication> authenResult = this.reactiveAuthenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword()));

    return authenResult
        .publishOn(Schedulers.boundedElastic())
        .map(authentication -> {
          TokenPair tokenPair = this.jwtUtils.createTokenPair((CustomUserDetails) authentication.getPrincipal());
          handleUpdateUsersRefreshToken(loginRequest.getUsername(), tokenPair.getRefreshToken());
          return this.authenResponseConverter.toAuthenResponse(this.mapper.map(authentication, EUser.class), tokenPair);
        });
  }

  @Override
  public AuthResponse handleRegister(RegisterRequest registerRequest) {
    this.userRepository
        .findUserByUsername(registerRequest.getUsername())
        .ifPresent(user -> {
          throw new RuntimeException("User already exists");
        });
    EUser user = this.userConverter.toUserEntity(registerRequest);
    ERole customerRole = this.mapper.map(this.roleRepository.findByCode("admin"), ERole.class);
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    return this.authenResponseConverter.toAuthenResponse(user, null);
  }

  @Override
  public Mono<Void> handleUpdateUsersRefreshToken(String username, String refreshToken) {
    EUser user = this.userRepository.findUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Not found user with username " + username));
    user.setRefreshToken(refreshToken);
    this.userRepository.save(user);
    return Mono.empty();
  }

  @Override
  public AuthResponse handleGetAuthenResponseByUsernameAndRefreshToken(String username, String refreshToken) {
    EUser user = this.userRepository.findByUsernameAndRefreshToken(username, refreshToken)
        .orElseThrow(() -> new UsernameNotFoundException("Not found user with refresh token and username " + username));
    TokenPair tokenPair = this.jwtUtils.createTokenPair(this.mapper.map(user, CustomUserDetails.class));
    handleUpdateUsersRefreshToken(username, tokenPair.getRefreshToken());
    return this.authenResponseConverter.toAuthenResponse(user, tokenPair);
  }

  @Override
  public void handleLogout(String username) {
    EUser user = this.userRepository.findUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Not found user with username " + username));
    user.setRefreshToken(null);
    this.userRepository.save(user);
  }

}
