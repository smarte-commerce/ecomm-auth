package com.winnguyen1905.auth.util;

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

import com.winnguyen1905.auth.core.model.response.CustomUserDetails;
import com.winnguyen1905.auth.core.model.response.RoleVm;

@Component
@PropertySource("classpath:application.yaml")
public class JwtUtils {
  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

  @Autowired
  private JwtEncoder jwtEncoder;

  @Value("${jwt.access_token-validity-in-seconds}")
  private String jwtAccessTokenExpiration;

  @Value("${jwt.refresh_token-validity-in-seconds}")
  private String jwtRefreshTokenExpiration;

  public JwtClaimsSet createJwtClaimsSet(CustomUserDetails userDetails, Instant now, Instant validity) {
    // List<PermissionVm> permissions = 
    // userDetails.role() instanceof RoleVm
    //     ? userDetails.role().permissions()
    //     : 
        new ArrayList<>();
    return JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(userDetails.getUsername() + "/" + userDetails.id())
        .claim("user", userDetails)
        .claim("permissions", null)
        .build();
  }

  public TokenPair createTokenPair(CustomUserDetails userDetails) {
    Instant now = Instant.now(),
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
