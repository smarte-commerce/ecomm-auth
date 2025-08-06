package com.winnguyen1905.auth.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Order(1)
@Configuration
@EnableWebSecurity
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
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable()) // CSRF explicitly disabled
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/auth/register", "/auth/login", "/auth/oauth2/**", "/v1/auth/register").permitAll()
            .requestMatchers("/ws/events").permitAll()
            .requestMatchers("/swagger-ui/**", "*-docs/**", "/webjars/**").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
          if (keycloakEnabled) {
            jwt.decoder(keycloakJwtDecoder());
          }
        }))
        .build();
  }

  /**
   * JWT Decoder for Keycloak tokens
   */
  @Bean
  @ConditionalOnProperty(name = "keycloak.direct-access-grants-enabled", havingValue = "true")
  JwtDecoder keycloakJwtDecoder() {
    if (keycloakEnabled && jwkSetUri != null && !jwkSetUri.isEmpty()) {
      return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    } else {
      // Fallback to master realm (default Keycloak setup)
      return NimbusJwtDecoder.withJwkSetUri("http://localhost:8087/realms/master/protocol/openid-connect/certs")
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
  CorsConfigurationSource corsConfigurationSource() {
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
