package com.winnguyen1905.auth.core.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winnguyen1905.auth.core.model.request.AbstractModel;
import com.winnguyen1905.auth.util.TokenPair;

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
