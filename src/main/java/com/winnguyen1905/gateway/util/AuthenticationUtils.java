package com.winnguyen1905.gateway.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.winnguyen1905.gateway.core.model.request.LoginRequest;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationUtils implements ReactiveAuthenticationManager {
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public Authentication authentication(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return authentication;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticate'");
    }
}