package com.winnguyen1905.gateway.core.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winnguyen1905.gateway.core.model.User;
import com.winnguyen1905.gateway.core.model.request.RegisterRequest;
import com.winnguyen1905.gateway.persistance.entity.ECustomer;
import com.winnguyen1905.gateway.persistance.entity.EShop;
import com.winnguyen1905.gateway.persistance.entity.EUserCredentials;

@Component
public class UserConverter {

  @Autowired
  public ModelMapper modelMapper;

  public <T> EUserCredentials toUserEntity(T object) {
    EUserCredentials user = new EUserCredentials();
    if (object instanceof RegisterRequest registerRequest) {
      user = modelMapper.map(registerRequest, EUserCredentials.class);
    } else {

    }
    return user;
  }

  public User toUser(EUserCredentials user) {
    User User = modelMapper.map(user, User.class);
    return User;
  }

  public ECustomer toECustomer(EUserCredentials user) {
    ECustomer customer = this.modelMapper.map(user, ECustomer.class);
    return customer;
  }

  public EShop toEShop(EUserCredentials user) {
    EShop shop = this.modelMapper.map(user, EShop.class);
    return shop;
  }
}
