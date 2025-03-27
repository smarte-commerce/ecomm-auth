package com.winnguyen1905.auth.core.service;

import java.util.UUID;

import com.winnguyen1905.auth.core.model.response.AccountVm;

public interface IAccountService {
  public AccountVm handleGetUserByUsername(String username);

  public AccountVm handleGetUserById(UUID id);

  public AccountVm handleUpdateUser(UUID id);
}
