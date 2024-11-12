package com.winnguyen1905.gateway.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.winnguyen1905.gateway.core.model.CustomUserDetails;
import com.winnguyen1905.gateway.core.model.Permission;
import com.winnguyen1905.gateway.core.model.Role;

@Component
// @PropertySource("classpath:application-dev.properties")
public class JwtUtils {
  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

  @Autowired
  private JwtEncoder jwtEncoder;

  @Value("${jwt.access_token-validity-in-seconds}")
  private String jwtAccessTokenExpiration;

  @Value("${jwt.refresh_token-validity-in-seconds}")
  private String jwtRefreshTokenExpiration;

  public JwtClaimsSet createJwtClaimsSet(CustomUserDetails userDetails, Instant now, Instant validity) {
    List<Permission> permissions = userDetails.getRole() instanceof Role
        ? userDetails.getRole().getPermissions()
        : new ArrayList<>();
    userDetails.setRole(null);
    return JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(userDetails.getUsername() + "/" + userDetails.getId())
        .claim("user", userDetails)
        .claim("permissions", permissions)
        .build();
  }

  public TokenPair createTokenPair(CustomUserDetails userDetails) {
    userDetails.setPassword(null);
    Instant 
        now = Instant.now(),
        accessTokenValidity = now.plus(Long.parseLong(jwtAccessTokenExpiration), ChronoUnit.SECONDS),
        refreshTokenValidity = now.plus(Long.parseLong(jwtRefreshTokenExpiration), ChronoUnit.SECONDS);
    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    String accessToken = jwtEncoder.encode(JwtEncoderParameters
        .from(jwsHeader, createJwtClaimsSet(userDetails, now, accessTokenValidity))).getTokenValue();
    String refreshToken = jwtEncoder.encode(JwtEncoderParameters
        .from(jwsHeader, createJwtClaimsSet(userDetails, now, refreshTokenValidity))).getTokenValue();
    return TokenPair.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken).build();
  }
}
