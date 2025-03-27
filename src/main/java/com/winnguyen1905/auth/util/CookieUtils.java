package com.winnguyen1905.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
  private static final String jwtRefreshTokenExpiration = "90000000";

  public static ResponseCookie deleteCookie(String name) {
    return ResponseCookie
        .from(name, "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();
  }

  public static ResponseCookie createCookie(String name, String refreshToken) {
    return ResponseCookie
        .from(name, refreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Long.parseLong(jwtRefreshTokenExpiration))
        .build();
  }
}
