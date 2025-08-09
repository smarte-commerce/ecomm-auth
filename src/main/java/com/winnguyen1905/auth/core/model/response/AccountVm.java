package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

import com.winnguyen1905.auth.common.constant.AccountType;
import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;

public record AccountVm(
    UUID id,
    String name,
    String email,
    String phone,
    String username,
    String password,
    Boolean status,
    AccountType accountType,
    String refreshToken) implements AbstractModel {
  @Builder
  public AccountVm(
      UUID id,
      String name,
      String email,
      String phone,
      String username,
      String password,
      Boolean status,
      AccountType accountType,
      String refreshToken) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.username = username;
    this.password = password;
    this.status = status;
    this.accountType = accountType;
    this.refreshToken = refreshToken;
  }
}
