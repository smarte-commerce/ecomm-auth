package com.winnguyen1905.auth.core.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.winnguyen1905.auth.core.model.request.AbstractModel;
import com.winnguyen1905.auth.util.TokenPair;

import lombok.Builder;

@Builder
public record AuthResponse(TokenPair tokens, AccountVm account) implements AbstractModel {
    @Builder
    public AuthResponse(TokenPair tokens, AccountVm account) {
        this.tokens = tokens;
        this.account = account;
    }

}
