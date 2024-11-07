package com.winnguyen1905.gateway.core.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component; 

import com.winnguyen1905.gateway.core.model.User;
import com.winnguyen1905.gateway.core.model.response.AuthResponse;
import com.winnguyen1905.gateway.persistance.entity.EUser;

@Component
public class AuthenResponseConverter {
    @Autowired
    public ModelMapper modelMapper;

    public AuthResponse toAuthenResponse(EUser user, Pair<String, String> tokenPair) {
        if (tokenPair == null) return AuthResponse.builder().user(this.modelMapper.map(user, User.class)).build();
        return AuthResponse.builder()
                .user(this.modelMapper.map(user, User.class))
                .accessToken(tokenPair.getFirst())
                .refreshToken(tokenPair.getSecond())
                .build();
    }
}