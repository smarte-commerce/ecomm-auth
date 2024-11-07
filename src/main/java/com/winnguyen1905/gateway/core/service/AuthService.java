package com.winnguyen1905.gateway.core.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.core.converter.AuthenResponseConverter;
import com.winnguyen1905.gateway.core.converter.UserConverter;
import com.winnguyen1905.gateway.core.model.CustomUserDetails;
import com.winnguyen1905.gateway.core.model.request.LoginRequest;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;
import com.winnguyen1905.gateway.persistance.entity.ERole;
import com.winnguyen1905.gateway.persistance.entity.EUser;
import com.winnguyen1905.gateway.persistance.repository.RoleRepository;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;
import com.winnguyen1905.gateway.util.AuthenticationUtils;
import com.winnguyen1905.gateway.util.JwtUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationUtils authenticationUtils;
    private final AuthenResponseConverter authenResponseConverter;

    @Override
    public Mono<AuthResponse> handleLogin(LoginRequest loginRequest) {
        Mono<Authentication> authenResult = this.authenticationUtils.authentication(loginRequest);
        Pair<String, String> tokenPair = this.jwtUtils
                .createTokenPair((CustomUserDetails) authenResult.getPrincipal());
        handleUpdateUsersRefreshToken(loginRequest.getUsername(), tokenPair.getSecond());
        return authenResponseConverter
                .toAuthenResponse(
                        this.modelMapper.map((CustomUserDetails) authenResult.getPrincipal(), EUser.class),
                        tokenPair);
    }

    @Override
    public AuthResponse handleRegister(RegisterRequest registerRequest) {
        EUser user = this.userConverter.toUserEntity(registerRequest);
        ERole customerRole = this.modelMapper.map(this.roleRepository.findByCode("admin"), ERole.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        // user.setRole(customerRole);
        user = this.userRepository.save(user);
        return this.authenResponseConverter.toAuthenResponse(user, null);
    }

    @Override
    public Void handleUpdateUsersRefreshToken(String username, String refreshToken) {
        EUser user = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with username " + username));
        user.setRefreshToken(refreshToken);
        this.userRepository.save(user);
        return null;
    }

    @Override
    public AuthResponse handleGetAuthenResponseByUsernameAndRefreshToken(String username, String refreshToken) {
        EUser user = this.userRepository.findByUsernameAndRefreshToken(username, refreshToken)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "Not found user with refresh token and username " + username));
        Pair<String, String> tokenPair = this.jwtUtils
                .createTokenPair(this.modelMapper.map(user, CustomUserDetails.class));
        handleUpdateUsersRefreshToken(username, tokenPair.getSecond());
        return this.authenResponseConverter.toAuthenResponse(user, tokenPair);
    }

    @Override
    public Void handleLogout(String username) {
        EUser user = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with username " + username));
        user.setRefreshToken(null);
        this.userRepository.save(user);
        return null;
    }

}