package com.winnguyen1905.gateway.core.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.converter.AuthenResponseConverter;
import com.winnguyen1905.gateway.converter.UserConverter;
import com.winnguyen1905.gateway.entity.RoleEntity;
import com.winnguyen1905.gateway.entity.UserEntity;
import com.winnguyen1905.gateway.exception.CustomRuntimeException;
import com.winnguyen1905.gateway.model.MyUserDetails;
import com.winnguyen1905.gateway.model.request.LoginRequest;
import com.winnguyen1905.gateway.model.request.RegisterRequest;
import com.winnguyen1905.gateway.model.response.AuthResponse;
import com.winnguyen1905.gateway.repository.RoleRepository;
import com.winnguyen1905.gateway.repository.UserRepository;
import com.winnguyen1905.gateway.service.IAuthService;
import com.winnguyen1905.gateway.util.AuthenticationUtils;
import com.winnguyen1905.gateway.util.JwtUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
    public AuthResponse handleLogin(LoginRequest loginRequest) {
        Authentication authenticResults = this.authenticationUtils.authentication(loginRequest);
        Pair<String, String> tokenPair = this.jwtUtils.createTokenPair((MyUserDetails) authenticResults.getPrincipal());
        handleUpdateUsersRefreshToken(loginRequest.getUsername(), tokenPair.getSecond());
        return authenResponseConverter
                .toAuthenResponse(
                        this.modelMapper.map((MyUserDetails) authenticResults.getPrincipal(), UserEntity.class),
                        tokenPair);
    }

    @Override
    public AuthResponse handleRegister(RegisterRequest registerRequest) {
        UserEntity user = this.userConverter.toUserEntity(registerRequest);
        RoleEntity customerRole = this.modelMapper.map(this.roleRepository.findByCode("admin"), RoleEntity.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRole(customerRole);
        user = this.userRepository.save(user);
        return this.authenResponseConverter.toAuthenResponse(user, null);
    }

    @Override
    public Void handleUpdateUsersRefreshToken(String username, String refreshToken) {
        UserEntity user = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException("Not found user with username " + username));
        user.setRefreshToken(refreshToken);
        this.userRepository.save(user);
        return null;
    }

    @Override
    public AuthResponse handleGetAuthenResponseByUsernameAndRefreshToken(String username, String refreshToken) {
        UserEntity user = this.userRepository.findByUsernameAndRefreshToken(username, refreshToken)
                .orElseThrow(
                        () -> new CustomRuntimeException("Not found user with refresh token and username " + username));
        Pair<String, String> tokenPair = this.jwtUtils.createTokenPair(this.modelMapper.map(user, MyUserDetails.class));
        handleUpdateUsersRefreshToken(username, tokenPair.getSecond());
        return this.authenResponseConverter.toAuthenResponse(user, tokenPair);
    }

    @Override
    public Void handleLogout(String username) {
        UserEntity user = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException("Not found user with username " + username));
        user.setRefreshToken(null);
        this.userRepository.save(user);
        return null;
    }

}