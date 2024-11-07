package com.winnguyen1905.gateway.core.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winnguyen1905.gateway.core.model.User;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.persistance.entity.ECustomer;
import com.winnguyen1905.gateway.persistance.entity.EShop;
import com.winnguyen1905.gateway.persistance.entity.EUser;

@Component
public class UserConverter {

    @Autowired public ModelMapper modelMapper;

    public <T> EUser toUserEntity(T object) {
        EUser user = new EUser();
        if(object instanceof RegisterRequest registerRequest) {
            user = modelMapper.map(registerRequest, EUser.class);
        } else {

        }
        return user;
    }

    public User toUser(EUser user) {
        User User = modelMapper.map(user, User.class);
        return User;
    }

    public ECustomer toECustomer(EUser user) {
        ECustomer customer = this.modelMapper.map(user, ECustomer.class);
        return customer;
    }

    public EShop toEShop(EUser user) {
        EShop shop = this.modelMapper.map(user, EShop.class);
        return shop;
    }
}