package com.winnguyen1905.gateway.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winnguyen1905.gateway.core.model.Role;
import com.winnguyen1905.gateway.core.service.IRoleService;
import com.winnguyen1905.gateway.util.MetaMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
public class RoleController {
  @Autowired
  private IRoleService roleService;

  @PostMapping
  @MetaMessage(message = "add new role success")
  public ResponseEntity<Role> addRole(
      @RequestBody Role role) {
    return ResponseEntity.status(HttpStatus.CREATED.value()).body(this.roleService.handleCreateRole(role));
  }
}
