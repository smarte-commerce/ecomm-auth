package com.winnguyen1905.gateway.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.winnguyen1905.gateway.core.model.CustomUserDetails;
import com.winnguyen1905.gateway.persistance.entity.EUserCredentials;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;

import reactor.core.publisher.Mono;

@Configuration
public class AuthenticationConfig {
  @Bean
  ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    authenticationManager.setPasswordEncoder(passwordEncoder);
    return authenticationManager;
  }

  @Bean
  ReactiveUserDetailsService reactiveUserDetailsService(ModelMapper mapper, UserRepository userRepository) {
    return username -> Mono.fromCallable(() -> {
      EUserCredentials user = userRepository.findUserByUsername(username)
          .orElseThrow(
            () -> new UsernameNotFoundException("Not found user by username")
            );
      return (UserDetails) mapper.map(user, CustomUserDetails.class);
    });
  }
}
