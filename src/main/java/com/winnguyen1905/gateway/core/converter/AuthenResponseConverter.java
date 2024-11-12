package com.winnguyen1905.gateway.core.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component; 

import com.winnguyen1905.gateway.core.model.User;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;
import com.winnguyen1905.gateway.persistance.entity.EUserCredentials;
import com.winnguyen1905.gateway.util.TokenPair;

@Component
public class AuthenResponseConverter {
    @Autowired
    public ModelMapper modelMapper;

    public AuthResponse toAuthenResponse(EUserCredentials user, TokenPair tokenPair) {
        return AuthResponse.builder()
            .user(this.modelMapper.map(user, User.class))
            .tokens(tokenPair).build();
    }
}
