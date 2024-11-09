package com.winnguyen1905.gateway.core.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Role extends BaseObject<Role> {
  private String name;
  private String code;
  private List<Permission> permissions;
  private List<String> permissionCodes;
}
