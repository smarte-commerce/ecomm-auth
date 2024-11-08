package com.winnguyen1905.gateway.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("${release.api.prefix}/roles")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @PostMapping
    @MetaMessage(message = "add new role success")
    public ResponseEntity<RoleDTO> addRole(
        @RequestBody RoleDTO roleDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(this.roleService.handleCreateRole(roleDTO));
    }   
}
