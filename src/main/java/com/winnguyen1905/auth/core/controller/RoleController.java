package com.winnguyen1905.auth.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winnguyen1905.auth.core.model.response.RoleVm;
import com.winnguyen1905.auth.core.service.IRoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
public class RoleController {
  // @Autowired
  // private IRoleService roleService;

  // @PostMapping
  // @MetaMessage(message = "add new role success")
  // public ResponseEntity<RoleVm> addRole(
  //     @RequestBody RoleVm role) {
  //   return ResponseEntity.status(HttpStatus.CREATED.value()).body(this.roleService.handleCreateRole(role));
  // }
}
