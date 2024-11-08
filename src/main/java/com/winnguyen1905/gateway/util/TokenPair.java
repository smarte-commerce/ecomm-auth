package com.winnguyen1905.gateway.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.winnguyen1905.gateway.core.model.AbstractModel;

import lombok.*;

@Getter
@Setter
@Builder
public class TokenPair extends AbstractModel {
    private String accessToken;
    @JsonIgnore private String refreshToken;
}
