package com.winnguyen1905.auth.core.model.response;

import java.util.UUID;

import com.winnguyen1905.auth.common.constant.AccountType;
import com.winnguyen1905.auth.core.model.request.AbstractModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public record AccountVm(
    UUID id,
    String name,
    String email,
    String phone,
    String username,
    AccountType accountType) implements AbstractModel {
  @Builder
  public AccountVm(
      UUID id,
      String name,
      String email,
      String phone,
      String username,
      AccountType accountType) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.username = username;
    this.accountType = accountType;
  }
}
