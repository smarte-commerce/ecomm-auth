package com.winnguyen1905.gateway.core.service;

import java.util.UUID;

import com.winnguyen1905.gateway.core.model.User;


public interface IUserService {
    public User handleGetUserByUsername(String username);

    public User handleGetUserById(UUID id);

    public User handleUpdateUser(UUID id);
}