package com.winnguyen1905.auth.common.constant;

public enum AccountType {
  ADMIN("ADMIN"), VENDOR("VENDOR"), CUSTOMER("CUSTOMER");

  String role;

  AccountType(String role) {
    this.role = role;
  }
}
