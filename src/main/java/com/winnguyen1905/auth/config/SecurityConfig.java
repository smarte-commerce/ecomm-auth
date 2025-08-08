package com.winnguyen1905.auth.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Order(1)
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Value("${keycloak.direct-access-grants-enabled:true}")
  private boolean keycloakEnabled;

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
  private String jwkSetUri;

  @Bean
  WebProperties.Resources resources() {
    return new WebProperties.Resources();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable()) // CSRF explicitly disabled
        .authorizeExchange(authz -> authz
            .pathMatchers("/auth/register", "/auth/login", "/auth/oauth2/**", "/v1/auth/register").permitAll()
            .pathMatchers("/auth/status", "/auth/health", "/health").permitAll()
            .pathMatchers("/ws/events").permitAll()
            .pathMatchers("/swagger-ui/**", "*-docs/**", "/webjars/**").permitAll()
            .pathMatchers("/actuator/**").permitAll()
            .anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
          if (keycloakEnabled) {
            jwt.jwtDecoder(keycloakJwtDecoder());
          }
        }))
        .build();
  }

  /**
   * JWT Decoder for Keycloak tokens
   */
  @Bean
  @ConditionalOnProperty(name = "keycloak.direct-access-grants-enabled", havingValue = "true")
  ReactiveJwtDecoder keycloakJwtDecoder() {
    if (keycloakEnabled && jwkSetUri != null && !jwkSetUri.isEmpty()) {
      return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    } else {
      // Fallback to master realm (default Keycloak setup)
      return NimbusReactiveJwtDecoder.withJwkSetUri("http://localhost:8087/realms/master/protocol/openid-connect/certs")
          .build();
    }
  }

  /**
   * WebClient builder for Keycloak service
   */
  @Bean
  org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder() {
    return org.springframework.web.reactive.function.client.WebClient.builder();
  }

  @Bean
  org.springframework.web.cors.reactive.CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000", "https://localhost:4173", "*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
    configuration.setAllowCredentials(true);
    configuration.addAllowedHeader("*");
    configuration
        .setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept", "x-no-retry", "x-api-key"));
    configuration.setMaxAge(3600L);
    org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
