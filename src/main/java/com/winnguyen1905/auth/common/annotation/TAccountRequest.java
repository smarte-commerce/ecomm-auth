package com.winnguyen1905.auth.common.annotation;

import java.util.UUID;

import com.winnguyen1905.auth.common.constant.AccountType;
import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

@Builder
public record TAccountRequest(
    UUID id,
    String username,
    AccountType role,
    UUID socketClientId) implements AbstractModel {
}
