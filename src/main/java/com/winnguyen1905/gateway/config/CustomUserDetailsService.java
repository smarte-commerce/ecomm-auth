package com.winnguyen1905.gateway.config;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.winnguyen1905.gateway.core.model.MyUserDetails;
import com.winnguyen1905.gateway.persistance.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component("reactiveUserDetailsService")
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return
            Mono.just(
                this.userRepository.findUserByUsername(username)
                .map(eUser -> this.mapper.map(eUser, MyUserDetails.class))
                .orElseThrow(() -> new UsernameNotFoundException("Username does not exist")));
    }
}