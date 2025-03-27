package com.winnguyen1905.auth.config;

import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import com.winnguyen1905.auth.exception.BaseException;
import com.winnguyen1905.auth.util.SecurityUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class JwtConfig {
  @Value("${jwt.base64-secret}")
  private String jwtKey;

  public SecretKey secretKey() {
    byte[] keyBytes = Base64.from(this.jwtKey).decode();
    return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtils.JWT_ALGORITHM.getName());
  }

  @Bean
  JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey()));
  }

  @Bean
  ReactiveJwtDecoder jwtDecoder() {
    NimbusReactiveJwtDecoder nimbusJwtDecoder = NimbusReactiveJwtDecoder
        .withSecretKey(secretKey())
        .macAlgorithm(SecurityUtils.JWT_ALGORITHM)
        .build();
    System.out.println(jwtKey);
    return token -> {
      try {
        System.out.println(jwtKey);
        return nimbusJwtDecoder.decode(token);
      } catch (Exception e) {
        System.out.println("Token error: " + token);
        throw new BaseException("refresh token invalid", 401);
      }
    };
  }

  // @Bean
  // JwtUtils jwtUtils() {
  // return new JwtUtils();
  // }

  // @Bean
  // JwtAuthenticationConverter jwtAuthenticationConverter() {
  // JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new
  // JwtGrantedAuthoritiesConverter();
  // grantedAuthoritiesConverter.setAuthorityPrefix("");
  // grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");
  // JwtAuthenticationConverter jwtAuthenticationConverter = new
  // JwtAuthenticationConverter();
  // jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
  // return jwtAuthenticationConverter;
  // }

  @Bean
  public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
    ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      List<String> roles = jwt.getClaimAsStringList("roles");
      return Flux.fromIterable(roles.stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
          .collect(Collectors.toList()));
    });
    return converter;
  }
}
