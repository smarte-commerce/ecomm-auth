package com.winnguyen1905.gateway.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component("reactiveAuthenticationManager")
public class CustomReactiveAuthManager implements ReactiveAuthenticationManager {
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    return Mono.just(authenticationManagerBuilder.getObject().authenticate(authentication))
        .onErrorResume(e -> Mono.error(new UsernameNotFoundException("Not found user: " + authentication.getName())));
  }
}
