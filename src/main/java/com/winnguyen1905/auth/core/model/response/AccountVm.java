package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

import com.winnguyen1905.auth.common.constant.AccountType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record AccountVm(
    UUID id,
    String name,
    String email,
    String phone,
    String username,
    AccountType accountType) {
}
