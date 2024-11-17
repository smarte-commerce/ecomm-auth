package com.winnguyen1905.gateway.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.winnguyen1905.gateway.core.model.CustomUserDetails;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;

import reactor.core.publisher.Mono;

@Configuration
public class AuthenticationConfig {
  @Bean
  ReactiveAuthenticationManager reactiveAuthenticationManager(
      ReactiveUserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    var authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    authManager.setPasswordEncoder(passwordEncoder);
    return authManager;
  }

  @Bean("reactiveUserDetailsService")
  ReactiveUserDetailsService reactiveUserDetailsService(ModelMapper mapper, UserRepository userRepository) {
    return username -> Mono.fromCallable(() -> userRepository.findUserByUsername(username))
        .flatMap(optionalUser ->
            Mono.justOrEmpty(optionalUser))
        // .switchIfEmpty(Mono.error(new UsernameNotFoundException("Not found user by username " + username)))
        .map(user ->
            mapper.map(user, CustomUserDetails.class));
  }
}
