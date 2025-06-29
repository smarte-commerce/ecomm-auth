package com.winnguyen1905.auth.core.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.winnguyen1905.auth.core.model.response.AccountVm;
import com.winnguyen1905.auth.persistance.entity.EAccountCredentials;
import com.winnguyen1905.auth.persistance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
  // private final UserRepository userRepository;
  // private final ModelMapper modelMapper;

  // @Override
  // public User handleGetUserByUsername(String username) {
  //   EUserCredentials user = this.userRepository.findUserByUsername(username)
  //       .orElseThrow(() -> new UsernameNotFoundException("Not found user by username " + username));
  //   return this.modelMapper.map(user, User.class);
  // }

  // @Override
  // public User handleGetUserById(UUID id) {
  //   EUserCredentials user = this.userRepository.findById(id)
  //       .orElseThrow(() -> (new UsernameNotFoundException("Username does not exist")));
  //   return this.modelMapper.map(user, User.class);
  // }

  // @Override
  // public User handleUpdateUser(UUID id) {
  //   throw new UnsupportedOperationException("Unimplemented method 'handleUpdateUser'");
  // }
}
