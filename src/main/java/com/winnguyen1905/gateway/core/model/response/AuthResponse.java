package com.winnguyen1905.gateway.core.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winnguyen1905.gateway.core.model.AbstractModel;
import com.winnguyen1905.gateway.core.model.User;
import com.winnguyen1905.gateway.util.TokenPair;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse extends AbstractModel {
    @JsonProperty("user")
    private User user;
    private TokenPair tokens;
}
