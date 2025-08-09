package com.winnguyen1905.auth.core.model.response;

import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

@Builder
public record AuthResponse(
  String accessToken,
  String refreshToken,
  String idToken,
  int expiresIn,
  int refreshExpiresIn,
  String tokenType,
  String scope,
  AccountVm account
) implements AbstractModel {}
