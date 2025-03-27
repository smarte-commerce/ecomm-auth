package com.winnguyen1905.auth.core.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winnguyen1905.auth.core.model.request.AbstractModel;
import com.winnguyen1905.auth.util.TokenPair;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record AuthResponse(TokenPair tokens) implements AbstractModel {
}
