package com.winnguyen1905.auth.config;

import java.util.Arrays;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Order(1)
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  public static final String[] whiteList = {
      "/**", "/auth/register", "/auth/**", "/v1/auth/login", "/v1/auth/refresh", "/storage/**", "/v1/products/**" };

  @Bean
  WebProperties.Resources resources() {
    return new WebProperties.Resources();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityWebFilterChain springWebFilterChain(
      ServerHttpSecurity http,
      ReactiveAuthenticationManager reactiveAuthenticationManager,
      CustomServerAuthenticationEntryPoint serverAuthenticationEntryPoint) {

    return http
        .cors(ServerHttpSecurity.CorsSpec::disable)
        .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF explicitly disabled
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .authenticationManager(reactiveAuthenticationManager)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
            .pathMatchers(SecurityConfig.whiteList).permitAll()
            .pathMatchers("/ws/events").permitAll()
            .pathMatchers("/auth/**", "/stripe/**", "/swagger-ui/**", "-docs/**", "/webjars/**").permitAll()
            .anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
            .authenticationEntryPoint(serverAuthenticationEntryPoint))
        .build(); // No .cors() configuration, effectively disabling CORS
  }

  @Bean
  UrlBasedCorsConfigurationSource reactiveCorsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000", "https://localhost:4173", "*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
    configuration.setAllowCredentials(true);
    configuration.addAllowedHeader("*");
    configuration
        .setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept", "x-no-retry", "x-api-key"));
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  
}
