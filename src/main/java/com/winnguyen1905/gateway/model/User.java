package com.winnguyen1905.gateway.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User extends BaseObject<User> {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean status;
    private String phone;
    private Role role;
    private String type;
}