package com.winnguyen1905.auth.core.model.response;

import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

@Builder
public record RestResponse<T>(
  T data,
  String error,
  Object message,
  Integer statusCode
) implements AbstractModel {}

