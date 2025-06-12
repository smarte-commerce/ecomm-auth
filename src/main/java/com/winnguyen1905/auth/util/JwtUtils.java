package com.winnguyen1905.auth.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.winnguyen1905.auth.common.constant.RegionPartition;
import com.winnguyen1905.auth.core.model.response.CustomUserDetails;
import com.winnguyen1905.auth.core.service.GeoLocationService;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.yaml")
public class JwtUtils {
  public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

  private final GeoLocationService geoLocationService;

  @Autowired
  private JwtEncoder jwtEncoder;

  @Value("${jwt.access_token-validity-in-seconds}")
  private String jwtAccessTokenExpiration;

  @Value("${jwt.refresh_token-validity-in-seconds}")
  private String jwtRefreshTokenExpiration;

  public JwtClaimsSet createJwtClaimsSet(CustomUserDetails userDetails, Instant now, Instant validity,
      RegionPartition region) {
    // List<PermissionVm> permissions =
    // userDetails.role() instanceof RoleVm
    // ? userDetails.role().permissions()
    // : new ArrayList<>();
    return JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(userDetails.id().toString())
        .claim("username", userDetails.username())
        .claim("role", userDetails.role())
        .claim("region", region)
        .build();
  }
  private String getClientIp(ServerHttpRequest request) {
    String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
        return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddress() != null
            ? request.getRemoteAddress().getAddress().getHostAddress()
            : "unknown";
}

  public TokenPair createTokenPair(CustomUserDetails userDetails, ServerWebExchange exchange) {
    var request = exchange.getRequest();
    String ipAddress = getClientIp(request);

    // // Check for X-Forwarded-For header (used by proxies/load balancers)
    // var forwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
    // if (forwardedFor != null && !forwardedFor.isEmpty()) {
    //   // X-Forwarded-For may contain multiple IPs; the first one is the client's
    //   ipAddress = forwardedFor.split(",")[0].trim();
    // } else {
    //   // Fallback to remote address if no X-Forwarded-For header
    //   var remoteAddress = request.getRemoteAddress();
    //   if (remoteAddress != null) {
    //     ipAddress = remoteAddress.getAddress().getHostAddress();
    //   }
    // }

    RegionPartition region = geoLocationService.getRegionFromIp(ipAddress);

    Instant now = Instant.now(),
        accessTokenValidity = now.plus(Long.parseLong(jwtAccessTokenExpiration), ChronoUnit.SECONDS),
        refreshTokenValidity = now.plus(Long.parseLong(jwtRefreshTokenExpiration), ChronoUnit.SECONDS);
    JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
    String accessToken = jwtEncoder.encode(JwtEncoderParameters
        .from(jwsHeader, createJwtClaimsSet(userDetails, now, accessTokenValidity, region))).getTokenValue();
    String refreshToken = jwtEncoder.encode(JwtEncoderParameters
        .from(jwsHeader, createJwtClaimsSet(userDetails, now, refreshTokenValidity, region))).getTokenValue();
    return TokenPair.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken).build();
  }
}
