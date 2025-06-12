package com.winnguyen1905.auth.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

@Builder
public record TokenPair(String accessToken, @JsonIgnore String refreshToken) implements AbstractModel {
    @Builder
    public TokenPair(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
