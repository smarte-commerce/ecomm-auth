package com.winnguyen1905.auth.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Order(1)
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Value("${keycloak.direct-access-grants-enabled:true}")
  private boolean keycloakEnabled;

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
  private String jwkSetUri;

  public static final String[] whiteList = {
      "/auth/register", "/auth/**", "/auth/oauth2/**", "/v1/auth/login", "/v1/auth/refresh",
      "/storage/**", "/v1/products/**", "/actuator/**" };

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
      CustomServerAuthenticationEntryPoint serverAuthenticationEntryPoint,
      ReactiveJwtDecoder jwtDecoder) {

    ServerHttpSecurity configuredHttp = http
        .cors(ServerHttpSecurity.CorsSpec::disable)
        .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF explicitly disabled
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .authenticationManager(reactiveAuthenticationManager)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
            .pathMatchers(SecurityConfig.whiteList).permitAll()
            .pathMatchers("/ws/events").permitAll()
            .pathMatchers("/auth/**", "/auth/oauth2/**", "/stripe/**", "/swagger-ui/**", "-docs/**", "/webjars/**")
            .permitAll()
            .anyExchange().authenticated())
        .oauth2ResourceServer(oauth2 -> {
          // Use the injected JWT decoder (Keycloak JWT validation)
          oauth2.jwt(jwt -> jwt.jwtDecoder(jwtDecoder));
          oauth2.authenticationEntryPoint(serverAuthenticationEntryPoint);
        });

    // OAuth2 client temporarily disabled for local development
    // if (keycloakEnabled) {
    // configuredHttp = configuredHttp.oauth2Client(Customizer.withDefaults());
    // }

    return configuredHttp.build();
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
