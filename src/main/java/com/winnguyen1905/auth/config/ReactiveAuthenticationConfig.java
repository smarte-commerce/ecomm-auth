package com.winnguyen1905.auth.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.winnguyen1905.auth.core.model.response.CustomUserDetails;
import com.winnguyen1905.auth.persistance.repository.UserRepository;

import reactor.core.publisher.Mono;

@Configuration
public class ReactiveAuthenticationConfig {
  @Bean
  ReactiveAuthenticationManager reactiveAuthenticationManager(
      ReactiveUserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    var authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    authManager.setPasswordEncoder(passwordEncoder);
    return authManager;
  }

  @Bean("reactiveUserDetailsService")
  ReactiveUserDetailsService reactiveUserDetailsService(UserRepository userRepository) {
    return username -> Mono.fromCallable(() -> userRepository.findUserByUsername(username))
        .flatMap(optionalUser -> Mono.justOrEmpty(optionalUser))
        .map(user -> CustomUserDetails.builder()
            .id(user.getId())
            .role(user.getAccountType())
            .email(user.getEmail())
            .phone(user.getPhone())
            .username(user.getUsername())
            .password(user.getPassword())
            .build());
  }
}
