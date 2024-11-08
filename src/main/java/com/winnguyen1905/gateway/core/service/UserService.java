package com.winnguyen1905.gateway.core.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.winnguyen1905.gateway.core.model.User;
import com.winnguyen1905.gateway.persistance.entity.EUser;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User handleGetUserByUsername(String username) {
        EUser user = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user by username " + username));
        return this.modelMapper.map(user, User.class);
    }

    @Override
    public User handleGetUserById(UUID id) {
      EUser user = this.userRepository.findById(id)
            .orElseThrow(() -> (new UsernameNotFoundException("Username does not exist")));
        return this.modelMapper.map(user, User.class);
    }

    @Override
    public User handleUpdateUser(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'handleUpdateUser'");
    }
}
