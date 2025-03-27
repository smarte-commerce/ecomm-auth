package com.winnguyen1905.auth.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;

@Builder
public record TokenPair(String accessToken, @JsonIgnore String refreshToken) {}
